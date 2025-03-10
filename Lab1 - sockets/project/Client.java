import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        int port = 12345;
        String hostname = "localhost";
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj swoje imię: ");
        String username = scanner.nextLine();

        try {
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket udpSendSocket = new DatagramSocket();
            DatagramSocket udpReceiveSocket = new DatagramSocket();

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

            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        udpReceiveSocket.receive(receivePacket);
                        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.print("\033[2K\r");
                        System.out.println(username + ": " + receivedMessage);
                        System.out.print(username + ": ");
                    }
                } catch (IOException e) {
                    System.out.println("Błąd w odbiorze UDP: " + e.getMessage());
                }
            }).start();

            out.println(username);
            out.println(udpSendSocket.getLocalPort());
            out.println(udpReceiveSocket.getLocalPort());

            while (true) {
                System.out.print(username + ": ");
                String message = scanner.nextLine();

                if (message.isEmpty()) continue;

                char protocol = message.charAt(0);
                String content = message.substring(1).trim();

                if (protocol == 'T' || protocol == 't') {
                    out.println(content);

                } else if (protocol == 'U' || protocol == 'u') {
                    byte[] sendBuffer = content.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                    udpSendSocket.send(sendPacket);

                } else {
                    System.out.println("Nieprawidłowy wybór protokołu. Wpisz 'T' lub 'U' na początku wiadomości.");
                }
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia: " + e.getMessage());
        }
    }
}