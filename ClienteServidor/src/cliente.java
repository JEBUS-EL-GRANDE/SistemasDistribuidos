import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class cliente {
    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 65432;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese número de cliente: ");
        int numero = sc.nextInt();
        sc.nextLine();

        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             Scanner teclado = new Scanner(System.in)) {

            System.out.println("\nCliente " + numero + " conectado al servidor.\n");

            for (int i = 1; i <= 3; i++) {
                System.out.print("Cliente " + numero + " - mensaje " + i + ": ");
                String mensaje = teclado.nextLine();

                salida.println(mensaje);
                String respuesta = entrada.readLine();

                System.out.println(" → Enviado: " + mensaje);
                System.out.println(" → Recibido: " + respuesta + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error en el cliente:");
            e.printStackTrace();
        }
    }
}
