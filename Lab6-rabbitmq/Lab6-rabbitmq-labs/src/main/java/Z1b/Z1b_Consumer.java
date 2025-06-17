package Z1b;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Z1b_Consumer {
    public static void main(String[] argv) throws Exception {

        System.out.println("Z1 CONSUMER");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String QUEUE_NAME = "queue1";
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
                try {
                    int timeToSleep = Integer.parseInt(message);
                    Thread.sleep(timeToSleep * 100);
                    System.out.println("Done processing: " + message);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Processing interrupted for message: " + message);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid message format (not a number): " + message);
                    channel.basicReject(envelope.getDeliveryTag(), false);
                }
            }
        };

        System.out.println("Waiting for messages...");
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
