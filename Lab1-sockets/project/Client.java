import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        int port = 12345;
        int multicastPort = 6989;
        String hostname = "localhost";
        String multicastHost = "228.5.6.8";
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj swoje imię: ");
        String username = scanner.nextLine();


        try {
            // utworzenie socketu TCP
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // utworzenie socketu UDP
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket udpSocket = new DatagramSocket(socket.getLocalPort());

            // utworzenie socketu Multicastowego
            InetAddress multicastAddress = InetAddress.getByName(multicastHost);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(new InetSocketAddress(multicastAddress, multicastPort), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));

            // wątek nasłuchujący TCP
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

            // wątek nasłuchujący UDP
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        udpSocket.receive(receivePacket);
                        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.print("\033[2K\r");
                        System.out.println(receivedMessage);
                        System.out.print(username + ": ");
                    }
                } catch (IOException e) {
                    System.out.println("Błąd w odbiorze UDP: " + e.getMessage());
                }
            }).start();

            // wątek nasłuchujący Multicast
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    while(true) {
                        multicastSocket.receive(receivePacket);
                        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                        String[] parts = receivedMessage .split(": ", 2);

                        if((!Objects.equals(parts[0], username))) {
                            System.out.print("\033[2K\r");
                            System.out.println(receivedMessage);
                            System.out.print(username + ": ");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Błąd w odbiorze Multicast: " + e.getMessage());
                }
            }).start();

            out.println(username);

            // wysyłanie wiadomości
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
                    udpSocket.send(sendPacket);

                } else if (protocol == 'M' || protocol == 'm') {
                    byte[] sendBuffer = (username + ": " + content).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, multicastAddress, multicastPort);
                    multicastSocket.send(sendPacket);
                }else {
                    System.out.println("Nieprawidłowy wybór protokołu. Wpisz 'T', 'U' lub 'M' na początku wiadomości.");
                }
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia: " + e.getMessage());
        }
    }
}