import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args) {
        int port = 12345;
        String hostname = "localhost";
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);


        System.out.print("Podaj swoje imię: ");
        String username = scanner.nextLine();

        try {
            socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(username);

            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        System.out.print("\033[2K\r");
                        System.out.println(receivedMessage);
                        System.out.print(username + ": ");
                    }
                } catch (IOException e) {
                    System.out.println("Połączenie z serwerem zostało przerwane.");
                }
            }).start();

            while (true) {
                System.out.print(username + ": ");
                String message = scanner.nextLine();
                out.println(message);
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


