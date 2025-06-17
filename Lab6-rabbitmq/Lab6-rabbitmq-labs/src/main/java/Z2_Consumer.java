import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Z2_Consumer {

//    private static final String EXCHANGE_NAME = "my_direct_exchange";
     private static final String EXCHANGE_NAME = "my_topic_exchange";

    public static void main(String[] argv) throws Exception {

        System.out.println("Z2 CONSUMER");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
         channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("Created queue: " + queueName);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter binding key(s) (e.g., 'log.error' or for topic 'log.*.error log.critical.*') or 'exit' to quit:");
        String inputKeys = br.readLine();

        if ("exit".equals(inputKeys)) {
            channel.close();
            connection.close();
            return;
        }

        String[] bindingKeys = inputKeys.split(" ");
        for (String key : bindingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, key.trim());
            System.out.println("Bound queue '" + queueName + "' with binding key '" + key.trim() + "'");
        }

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        System.out.println("Waiting for messages...");
        channel.basicConsume(queueName, true, consumer);
    }
}