import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import model.Order;

import java.io.*;
import java.util.*;


public class Supplier {
    private static final String EXCHANGE_NAME = "orders";
    private final String name;
    private final Set<String> supportedItems;
    private final ConnectionFactory factory;

    public Supplier(String name, Set<String> supportedItems) {
        this.name = name;
        this.supportedItems = supportedItems;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
    }

    @SneakyThrows
    public void start() {
        Connection connection = factory.newConnection();
        for (String item : supportedItems) {
            Channel channel = connection.createChannel();
            String queueName = "queue_" + item;
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, item);

            channel.basicQos(1);

            DeliverCallback callback = (consumerTag, delivery) -> {
                Order order = deserialize(delivery.getBody());
                String team = order.getTeamName();
                String gear = order.getItem();
                int orderNumber = order.getOrderNumber();

                System.out.println("[" + name + "] Received order from " + team + ": " + gear + " (Team Order #" + orderNumber + ")");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Supplier interrupted during processing", e);
                }
                sendConfirmation(team, gear, orderNumber);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            channel.basicConsume(queueName, false, callback, consumerTag -> {});
            System.out.println("[" + name + "] Listening for: " + item);
        }
    }

    @SneakyThrows
    private void sendConfirmation(String team, String item, int orderNumber) {
        try (Connection conn = factory.newConnection();
             Channel ch = conn.createChannel()) {
            ch.queueDeclare(team, false, false, false, null);
            String message = name + ": Order #" + orderNumber + " (" + item + ") completed";
            ch.basicPublish("", team, null, message.getBytes());
            System.out.println("[" + name + "] Sent confirmation to " + team + ": " + message);
        }
    }

    @SneakyThrows
    private Order deserialize(byte[] body) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body))) {
            return (Order) ois.readObject();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) return;
        String name = args[0];
        Set<String> items = new HashSet<>(Arrays.asList(args).subList(1, args.length));
        new Supplier(name, items).start();
    }
}