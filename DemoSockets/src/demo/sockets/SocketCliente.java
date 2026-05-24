/*package demo.sockets;
import java.io.BufferedReader;      //BufferedReader y InputStreamReader para leer texto de un InputStream.
import java.io.InputStreamReader ; 
import java.io.PrintWriter;         //para escribir texto en un OutputStream.
import java.net.Socket;             //para escuchar conexiones entrantes (lado servidor).
import java.nio.charset.StandardCharsets;//proporciona constantes de codificación (UTF_8).

public class SocketCliente{
    public static void main (String[] args){
        String host ="127.0.0.1" ;  
        int puerto = 6100;
        try( Socket socket = new Socket(host, puerto)) { //Crea un ServerSocket escuchando en el puerto 6100 y lo encierra en un try-with-resources para cerrarlo automáticamente al finalizar.
            System.out.println("Conectado a "+ host + ":" + puerto);
            PrintWriter salida = new PrintWriter( socket.getOutputStream(),true,StandardCharsets.UTF_8);
            BufferedReader entrada = new BufferedReader (
                new InputStreamReader (socket.getInputStream(),StandardCharsets.UTF_8)
            );
            String mensaje ="Hola servidor , desde el cliente";
            System.out.println("Cliente enviara:"+ mensaje );
            salida.println(mensaje);
            String respuesta = entrada.readLine();
            System.out.println("Respuesta recibida"+ respuesta);
            System.out.println("Hasta luego!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}*/
/*try {
    // Código que puede lanzar excepciones
} catch (Exception e) {
    // Código que se ejecuta SI ocurre un error
}*/


package demo.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketCliente {

    private static final String HOST = "127.0.0.1"; // Cambia por la IP del servidor en pruebas LAN
    private static final int PUERTO = 6100;

    public static void main(String[] args) {
        System.out.println("Cliente TCP - escribe texto y SALIR para terminar.");

        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {

            System.out.println("Conectado a " + HOST + ":" + PUERTO);

            while (true) {
                System.out.print("Ingrese texto (SALIR para terminar): ");
                String linea = scanner.nextLine();

                if ("SALIR".equalsIgnoreCase(linea.trim())) {
                    System.out.println("Cerrando cliente...");
                    break;
                }

                salida.println(linea);
                String respuesta = entrada.readLine();

                if (respuesta == null) {
                    System.out.println("Servidor cerró la conexión.");
                    break;
                }

                System.out.println("Respuesta del servidor: " + respuesta);
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente:");
            e.printStackTrace();
        }
    }
}