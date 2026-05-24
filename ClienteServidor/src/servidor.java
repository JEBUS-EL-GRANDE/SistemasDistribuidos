import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class servidor {
    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 65432;

    public static void main(String[] args) {
        System.out.println("Servidor TCP iniciado en " + HOST + ":" + PUERTO);
        try (ServerSocket server = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = server.accept();
                Thread hilo = new Thread(new ManejadorCliente(socketCliente));
                hilo.start();
            }
        } catch (Exception e) {
            System.err.println("Error en el servidor:");
            e.printStackTrace();
        }
    }
}
class ManejadorCliente implements Runnable {
    private Socket socket;
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        String ip = socket.getInetAddress().getHostAddress();
        int puerto = socket.getPort();
        System.out.println("\nCliente conectado desde " + ip + ":" + puerto);
        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true, StandardCharsets.UTF_8);
        ) {
            String mensaje;
            for (int i = 1; i <= 3; i++) {
                mensaje = entrada.readLine();
                if (mensaje == null) break;
                System.out.println("[Servidor] Recibido de "+ip+": "+puerto+"->"+ mensaje);
                String respuesta = mensaje.toUpperCase();
                salida.println(respuesta);
                System.out.println("[Servidor] Enviado a "+ip+": "+puerto+ "-> "+respuesta);
            }
        } catch (Exception e) {
            System.err.println("Error manejando cliente "+ip+": "+puerto);
            e.printStackTrace();
        }
        System.out.println("Cliente " + ip + ":" + puerto + " desconectado.");
    }
}