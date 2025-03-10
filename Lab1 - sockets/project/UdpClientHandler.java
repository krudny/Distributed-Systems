import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UdpClientHandler implements Runnable {
    private byte[] receiveBuffer = new byte[1024];
    private DatagramSocket udpSocket;
    private ConcurrentHashMap<InetSocketAddress, Integer> clientsAddresses;

    public UdpClientHandler(DatagramSocket socket, ConcurrentHashMap<InetSocketAddress, Integer> clientsAddresses) {
        this.udpSocket = socket;
        this.clientsAddresses = clientsAddresses;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                udpSocket.receive(receivePacket);

                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());

                for (InetSocketAddress client : clientsAddresses.keySet()) {
                    if(clientsAddresses.get(client) != receivePacket.getPort()) {
                        byte[] sendBuffer = msg.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.getAddress(), client.getPort());
                        udpSocket.send(sendPacket);
                    }
                }

            } catch (IOException e) {
                System.out.println("Błąd w obsłudze UDP: " + e.getMessage());
            }
        }
    }
}
