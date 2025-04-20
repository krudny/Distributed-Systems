import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class UdpClientHandler implements Runnable {
    private byte[] receiveBuffer = new byte[1024];
    private DatagramSocket udpSocket;
    private ConcurrentHashMap<Socket, String> connectedClients;

    public UdpClientHandler(DatagramSocket socket, ConcurrentHashMap<Socket, String> connectedClients) {
        this.udpSocket = socket;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        // rozsyłanie wiadomosci UDP do pozostalych klientów
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                udpSocket.receive(receivePacket);

                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());

                String username = "";

                for(Socket socket : connectedClients.keySet()) {
                    if(socket.getPort() == receivePacket.getPort()) {
                        username = connectedClients.get(socket);
                        break;
                    }
                }

                for(Socket socket : connectedClients.keySet()) {
                    if (receivePacket.getPort() != socket.getPort()) {
                        byte[] sendBuffer = (username + ": " + msg).getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, socket.getLocalAddress(), socket.getPort());
                        udpSocket.send(sendPacket);
                    }
                }


            } catch (IOException e) {
                System.out.println("Błąd w obsłudze UDP: " + e.getMessage());
            }
        }
    }
}
