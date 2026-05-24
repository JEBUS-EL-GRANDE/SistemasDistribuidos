import socket
import threading
import os
#el que almacena
#(múltiples instancias)
class DataNode:
    def __init__(self, host, port, storage_dir):
        self.host = host
        self.port = port
        self.storage_dir = storage_dir

        # Crear directorio si no existe
        if not os.path.exists(storage_dir):
            os.makedirs(storage_dir)

    def handle_client(self, conn, addr):
        print(f"[DataNode] Conexión desde {addr}")
        try:
            # Recibir comando (UPLOAD o DOWNLOAD)
            command = conn.recv(1024).decode().split()
            action = command[0]
            filename = command[1]

            path = os.path.join(self.storage_dir, filename)

            if action == "UPLOAD":
                conn.send(b"READY")  # Indicar que estamos listos
                with open(path, 'wb') as f:
                    while True:
                        data = conn.recv(4096)
                        if not data: break
                        f.write(data)
                print(f"[DataNode] Archivo {filename} guardado.")

            elif action == "DOWNLOAD":
                if os.path.exists(path):
                    conn.send(b"FOUND")
                    with open(path, 'rb') as f:
                        while True:
                            data = f.read(4096)
                            if not data: break
                            conn.send(data)
                else:
                    conn.send(b"NOT_FOUND")
                print(f"[DataNode] Archivo {filename} enviado.")

        except Exception as e:
            print(f"[DataNode] Error: {e}")
        finally:
            conn.close()

    def start(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((self.host, self.port))
        server.listen()
        print(f"[DataNode] Escuchando en {self.host}:{self.port}...")

        while True:
            conn, addr = server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()


if __name__ == "__main__":
    # Puedes cambiar el puerto para correr múltiples nodos (ej: 8001, 8002)
    import sys

    port = int(sys.argv[1]) if len(sys.argv) > 1 else 8001
    folder = sys.argv[2] if len(sys.argv) > 2 else "data_node_1"

    node = DataNode("localhost", port, folder)
    node.start()