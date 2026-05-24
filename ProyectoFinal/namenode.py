import socket
import threading
import json
#nuestro servidor de metadatos

class NameNode:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        # Tabla de archivos: { "archivo.txt": ["localhost", 8001] }
        self.file_map = {}
        # Lista de DataNodes disponibles (Round Robin simple)
        self.datanodes = [
            ("localhost", 8001),
            ("localhost", 8002)  # Asumimos que levantaremos un segundo nodo
        ]
        self.current_node_index = 0

    def get_next_datanode(self):
        # Algoritmo Round Robin para distribuir carga
        node = self.datanodes[self.current_node_index]
        self.current_node_index = (self.current_node_index + 1) % len(self.datanodes)
        return node

    def handle_client(self, conn, addr):
        print(f"[NameNode] Conexión desde {addr}")
        try:
            request = conn.recv(1024).decode().split()
            command = request[0]
            filename = request[1]

            response = {}

            if command == "UPLOAD":
                # Asignar un DataNode
                dn_host, dn_port = self.get_next_datanode()
                self.file_map[filename] = (dn_host, dn_port)
                response = {"status": "OK", "host": dn_host, "port": dn_port}
                print(f"[NameNode] Asignado {filename} a {dn_host}:{dn_port}")

            elif command == "DOWNLOAD":
                if filename in self.file_map:
                    dn_host, dn_port = self.file_map[filename]
                    response = {"status": "FOUND", "host": dn_host, "port": dn_port}
                else:
                    response = {"status": "NOT_FOUND"}

            conn.send(json.dumps(response).encode())

        except Exception as e:
            print(f"[NameNode] Error: {e}")
        finally:
            conn.close()

    def start(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((self.host, self.port))
        server.listen()
        print(f"[NameNode] Maestro escuchando en {self.host}:{self.port}...")

        while True:
            conn, addr = server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()


if __name__ == "__main__":
    namenode = NameNode("localhost", 8000)
    namenode.start()