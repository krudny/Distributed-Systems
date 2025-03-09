import java.io.*;
import java.net.*;
import java.util.*;

public class TcpServer {
    private static HashMap<Socket, String> connectedClients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serwer nasłuchuje na porcie " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket, connectedClients)).start();
        }
    }
}
