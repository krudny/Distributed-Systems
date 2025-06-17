package Z1a;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class Z1a_Producer {
    public static void main(String[] argv) throws Exception {

        System.out.println("Z1 PRODUCER");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String QUEUE_NAME = "queue1";
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message;
        System.out.println("Enter messages to send (type 'exit' to quit):");

        while (true) {
            message = br.readLine();
            if ("exit".equalsIgnoreCase(message)) {
                break;
            }
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent: " + message);
        }

        channel.close();
        connection.close();
    }
}