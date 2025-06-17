package Z1b;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Z1b_Producer {
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
        System.out.println("Enter messages to send (type 'exit' to quit):");

        String[] messages = {"1", "5", "1", "5", "1", "5", "1", "5", "1", "5"};

        for (String message : messages) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent: " + message);
            Thread.sleep(5);
        }

        System.out.println("All 10 messages sent. Type 'exit' to quit.");
        while (true) {
            String input = br.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
        }

        channel.close();
        connection.close();
    }
}
