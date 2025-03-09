import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {
    private final Socket currentSocket;
    private String username;
    private final HashMap<Socket, String> connectedClients;

    public ClientHandler(Socket socket, HashMap<Socket, String> connectedClients) {
        this.currentSocket = socket;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));


            username = in.readLine();
            connectedClients.put(currentSocket, username);
            System.out.println("Nowy użytkownik " + username + " dołączył do czatu!");

            String message;
            while ((message = in.readLine()) != null) {
                for(Socket socket : connectedClients.keySet()) {
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
