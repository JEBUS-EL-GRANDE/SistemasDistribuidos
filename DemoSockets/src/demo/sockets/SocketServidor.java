/*package demo.sockets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class SocketServidor {
    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(6100)) {
            System.out.println("Servidor TCP escuchando en 0.0.0.0:6100");
            try (Socket socketCliente = servidor.accept()) {
                System.out.println("Cliente conectado desde: "+ socketCliente.getInetAddress().getHostAddress()
                    + ":" + socketCliente.getPort());
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socketCliente.getInputStream(),StandardCharsets.UTF_8)
                );
                PrintWriter salida = new PrintWriter(
                        socketCliente.getOutputStream(),
                        true,
                        StandardCharsets.UTF_8
                );

                String lineaRecibida = entrada.readLine();
                System.out.println("Servidor recibió: " + lineaRecibida);

                String respuesta = "SOCKETS:" + lineaRecibida;
                salida.println(respuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/




package demo.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SocketServidor {

    private static final int PUERTO = 6100;
    private static final int MAX_CONEXIONES = 3;
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("Servidor TCP iniciando...");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en 0.0.0.0:" + PUERTO);

            for (int i = 1; i <= MAX_CONEXIONES; i++) {
                System.out.println("\nEsperando conexión #" + i + " ...");

                try (Socket socketCliente = servidor.accept();
                     BufferedReader entrada = new BufferedReader(
                             new InputStreamReader(socketCliente.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter salida = new PrintWriter(
                             socketCliente.getOutputStream(), true, StandardCharsets.UTF_8)) {

                    String direccionCliente = socketCliente.getInetAddress().getHostAddress();
                    int puertoCliente = socketCliente.getPort();
                    String lineaRecibida = entrada.readLine();

                    String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
                    System.out.printf("[%s] Cliente %s:%d -> %s%n",
                            timestamp, direccionCliente, puertoCliente, lineaRecibida);

                    String respuesta = "SOCKETS:" + lineaRecibida;
                    salida.println(respuesta);

                } catch (Exception e) {
                    System.err.println("Error manejando la conexión #" + i + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\nServidor ha aceptado las " + MAX_CONEXIONES + " conexiones requeridas. Finalizando.");

        } catch (Exception e) {
            System.err.println("Error en el servidor:");
            e.printStackTrace();
        }
    }
}