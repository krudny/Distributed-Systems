import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ClientHandler implements Runnable {
    private final Socket currentSocket;
    private String username;
    private final ConcurrentHashMap<Socket, String> connectedClients;

    public ClientHandler(Socket socket, ConcurrentHashMap<Socket, String> connectedClients){
        this.currentSocket = socket;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));

            // zapisanie nowego klienta TCP
            username = in.readLine();
            connectedClients.put(currentSocket, username);
            System.out.println("Nowy użytkownik " + username + " dołączył do czatu!");

            // rozsyłanie do pozostałych klientów
            String message;
            while ((message = in.readLine()) != null) {
                for (Socket socket : connectedClients.keySet()) {
                    if (socket != currentSocket) {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(username + ": " + message);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Użytkownik " + username + " rozłączył się.");
        } finally {
            try {
                if (currentSocket != null) {
                    connectedClients.remove(currentSocket);
                    currentSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
