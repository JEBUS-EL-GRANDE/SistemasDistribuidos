'''forma de uso
termianl 1: python namenode.py
termianl 2: python datanode.py 8001 data_node_1
termianl 3: python datanode.py 8002 data_node_2
termianl 4: echo "Hola mundo desde un sistema distribuido" > mi_archivo.txt
            python dfs_client.py upload mi_archivo.txt
            python dfs_client.py download mi_archivo.txt
sistema de archivos distribuidos
'''
import socket
import json
import os
import sys
#actua como cliente de namenode y datanode
NAMENODE_HOST = "localhost"
NAMENODE_PORT = 8000


def upload_file(filename):
    if not os.path.exists(filename):
        print("El archivo local no existe.")
        return

    # 1. Preguntar al NameNode dónde subir
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((NAMENODE_HOST, NAMENODE_PORT))
        sock.send(f"UPLOAD {filename}".encode())
        response = json.loads(sock.recv(1024).decode())
        sock.close()
    except ConnectionRefusedError:
        print("No se pudo conectar al NameNode.")
        return

    if response['status'] == 'OK':
        dn_host = response['host']
        dn_port = response['port']

        # 2. Subir al DataNode
        print(f"Subiendo {filename} a {dn_host}:{dn_port}...")
        try:
            dn_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            dn_sock.connect((dn_host, dn_port))
            dn_sock.send(f"UPLOAD {filename}".encode())

            ack = dn_sock.recv(1024)  # Esperar "READY"
            if ack == b"READY":
                with open(filename, 'rb') as f:
                    while True:
                        data = f.read(4096)
                        if not data: break
                        dn_sock.send(data)
                print("Subida completada.")
            dn_sock.close()
        except Exception as e:
            print(f"Error conectando al DataNode: {e}")


def download_file(filename):
    # 1. Preguntar al NameNode dónde está el archivo
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((NAMENODE_HOST, NAMENODE_PORT))
        sock.send(f"DOWNLOAD {filename}".encode())
        response = json.loads(sock.recv(1024).decode())
        sock.close()
    except ConnectionRefusedError:
        print("No se pudo conectar al NameNode.")
        return

    if response['status'] == 'FOUND':
        dn_host = response['host']
        dn_port = response['port']

        # 2. Descargar del DataNode
        print(f"Descargando {filename} desde {dn_host}:{dn_port}...")
        try:
            dn_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            dn_sock.connect((dn_host, dn_port))
            dn_sock.send(f"DOWNLOAD {filename}".encode())

            status = dn_sock.recv(1024)  # Esperar "FOUND" o "NOT_FOUND"
            if status == b"FOUND":
                with open(f"descargado_{filename}", 'wb') as f:
                    while True:
                        data = dn_sock.recv(4096)
                        if not data: break
                        f.write(data)
                print(f"Descarga completada: descargado_{filename}")
            else:
                print("El DataNode no encontró el archivo físico.")
            dn_sock.close()
        except Exception as e:
            print(f"Error conectando al DataNode: {e}")
    else:
        print("El archivo no existe en el sistema DFS.")


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python dfs_client.py [upload|download] [archivo]")
    else:
        command = sys.argv[1]
        file = sys.argv[2]

        if command == "upload":
            upload_file(file)
        elif command == "download":
            download_file(file)