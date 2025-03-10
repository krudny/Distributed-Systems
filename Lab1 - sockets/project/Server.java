import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ConcurrentHashMap<Socket, String> connectedClients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        DatagramSocket udpSocket = new DatagramSocket(port);

        System.out.println("Serwer nasłuchuje na porcie " + port);

        new Thread(new UdpClientHandler(udpSocket, connectedClients)).start();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket, connectedClients)).start();
        }
    }
}
