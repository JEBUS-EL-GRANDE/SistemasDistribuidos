# Sistemas Distribuidos (ELC-105) — Mini Proyectos

Este repositorio contiene **3 proyectos** desarrollados durante la materia de Sistemas Distribuidos. A continuación se explica el funcionamiento de cada uno.

---

## 1. ClienteServidor — Servidor Multi-Cliente en Java

**Lenguaje:** Java 1.8

### Descripción
Aplicacion TCP cliente-servidor que permite que **multiples clientes** se conecten al servidor de forma **concurrente**. Cada cliente envia **3 mensajes** y el servidor responde convirtiendolos a **mayusculas**.

### Arquitectura
- **Servidor** (`servidor.java`): Escucha en `127.0.0.1:65432`. Por cada cliente que se conecta, crea un hilo (`ManejadorCliente`) que atiende exclusivamente a ese cliente.
- **Cliente** (`cliente.java`): Se conecta al servidor, solicita al usuario 3 mensajes por consola, los envia y muestra la respuesta en mayúsculas.

### Flujo
1. El servidor inicia y queda en espera de conexiones.
2. El cliente se conecta y envía 3 líneas de texto.
3. El servidor recibe cada línea, la convierte a mayúsculas y la devuelve.
4. Al terminar los 3 intercambios, la conexión se cierra.

### Ejecución
```bash
# Terminal 1 — Servidor
java servidor

# Terminal 2 — Cliente
java cliente
```

---

## 2. DemoSockets — Demostración de Sockets TCP en Java

**Lenguaje:** Java 21

### Descripción
Proyecto de demostracion de comunicación mediante **sockets TCP**. El servidor acepta exactamente **3 conexiones consecutivas** (una tras otra, no concurrentes), procesa un mensaje por conexión y responde con el prefijo `SOCKETS:`. El cliente envia mensajes en un bucle hasta que el usuario escribe `SALIR`.

### Arquitectura
- **Servidor** (`SocketServidor.java`): Escucha en el puerto `6100`. Acepta 3 conexiones en un `for`; por cada una, lee una línea, registra la fecha/hora y la IP:puerto del cliente, y responde con `"SOCKETS:" + mensaje`.
- **Cliente** (`SocketCliente.java`): Bucle interactivo donde el usuario ingresa texto; al escribir `SALIR` se desconecta.

### Flujo
1. Servidor espera una conexion (hasta 3).
2. Cliente envía mensajes hasta que decida salir.
3. Servidor responde cada mensaje con el prefijo `SOCKETS:`.
4. Tras la terceraon, el servidor finaliza.

### Ejecución
```bash
# Terminal 1 — Servidor
java demo.sockets.SocketServidor

# Terminal 2 — Cliente
java demo.sockets.SocketCliente
```

---

## 3. ProyectoFinal — Sistema de Archivos Distribuido (DFS) en Python

**Lenguaje:** Python 3.13 (solo librería estándar)

### Descripción
Implementación simplificada de un **Sistema de Archivos Distribuido** inspirado en **HDFS**. Consta de 3 componentes:

- **NameNode** (`namenode.py`): Servidor de metadatos que sabe qué DataNode almacena cada archivo. Asigna archivos usando **Round Robin**.
- **DataNode** (`datanode.py`): Almacena físicamente los archivos en disco. Se pueden ejecutar múltiples instancias (puertos 8001, 8002, etc.).
- **Cliente** (`dfs_client.py`): Interfaz de línea de comandos para subir (`upload`) y descargar (`download`) archivos.

### Arquitectura
```
Cliente  ──►  NameNode (localhost:8000)  ──►  DataNode 1 (localhost:8001)
                                        ──►  DataNode 2 (localhost:8002)
```

### Flujo de subida (`upload`)
1. Cliente pregunta al NameNode dónde guardar el archivo.
2. NameNode asigna un DataNode (Round Robin) y responde con IP y puerto.
3. Cliente envía el archivo directamente al DataNode asignado.

### Flujo de descarga (`download`)
1. Cliente pregunta al NameNode dónde está el archivo.
2. NameNode responde con la ubicación del DataNode que lo almacena.
3. Cliente solicita el archivo directamente al DataNode y lo guarda localmente.

### Ejecución
```bash
# Terminal 1 — NameNode
python namenode.py

# Terminal 2 — DataNode 1
python datanode.py 8001 data_node_1

# Terminal 3 — DataNode 2
python datanode.py 8002 data_node_2

# Terminal 4 — Cliente
python dfs_client.py upload mi_archivo.txt
python dfs_client.py download mi_archivo.txt
```

---

## Resumen comparativo

| Proyecto        | Lenguaje  | Puerto(s)       | Concurrencia | Propósito principal                    |
|-----------------|-----------|-----------------|--------------|----------------------------------------|
| ClienteServidor | Java 1.8  | 65432           | Sí (hilos)   | Servidor multi-cliente con eco en mayúsculas |
| DemoSockets     | Java 21   | 6100            | No           | Demostración básica de sockets TCP     |
| ProyectoFinal   | Python 3  | 8000, 8001, 8002| Sí (hilos)   | Sistema de archivos distribuido (DFS)  |
