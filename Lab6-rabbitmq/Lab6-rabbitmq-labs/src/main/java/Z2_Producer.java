import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Z2_Producer {

//    private static final String EXCHANGE_NAME = "my_direct_exchange";
     private static final String EXCHANGE_NAME = "my_topic_exchange";

    public static void main(String[] argv) throws Exception {

        System.out.println("Z2 PRODUCER");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
         channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter routing key and message (e.g., 'log.info This is an info message') or 'exit' to quit:");

        while (true) {
            String inputLine = br.readLine();
            if ("exit".equals(inputLine)) {
                break;
            }

            int firstSpace = inputLine.indexOf(' ');
            if (firstSpace == -1) {
                System.out.println("Invalid input format. Please enter 'routing_key message'.");
                continue;
            }

            String routingKey = inputLine.substring(0, firstSpace);
            String message = inputLine.substring(firstSpace + 1);

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("Sent '" + routingKey + "':'" + message + "'");
        }

        channel.close();
        connection.close();
    }
}