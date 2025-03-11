import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class JavaServer {

    public static void main(String args[])
    {
        System.out.println("JAVA UDP SERVER");
        DatagramSocket socket = null;
        int portNumber = 9012;

        try{
            socket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[1024];

            while(true) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                ByteBuffer wrapped = ByteBuffer.wrap(receiveBuffer, 0, 4);
                wrapped.order(ByteOrder.LITTLE_ENDIAN);
                int receivedNumber = wrapped.getInt();
                System.out.println("Odebrana liczba: " + receivedNumber);

                int sendNumber = receivedNumber + 1;

                ByteBuffer sendBuffer = ByteBuffer.allocate(4);
                sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
                sendBuffer.putInt(sendNumber);
                byte[] sendBytes = sendBuffer.array();

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}