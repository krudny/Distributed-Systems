import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import model.Order;

import java.io.*;
import java.util.List;

public class Team {
    private static final String EXCHANGE_NAME = "orders";
    private final String name;
    private final List<String> requests;
    private final ConnectionFactory factory;

    public Team(String name, List<String> requests) {
        this.name = name;
        this.requests = requests;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
    }

    @SneakyThrows
    public void start() {
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(name, false, false, false, null);

        new Thread(() -> {
            try {
                Channel confirmChannel = conn.createChannel();
                DeliverCallback callback = (consumerTag, delivery) -> {
                    String msg = new String(delivery.getBody());
                    System.out.println("[" + name + "] Received confirmation: " + msg);
                };
                confirmChannel.basicConsume(name, true, callback, consumerTag -> {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        for (String item : requests) {
            Order order = new Order(name, item);
            byte[] body = serialize(order);
            channel.basicPublish(EXCHANGE_NAME, item, null, body);
            System.out.println("[" + name + "] Sent order: " + order.getItem() + " (Order #" + order.getOrderNumber() + ")");
            Thread.sleep(300);
        }
    }

    private byte[] serialize(Order order) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(order);
            oos.flush();
        }
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        String name = args[0];
        List<String> orders = List.of(args).subList(1, args.length);
        new Team(name, orders).start();
    }
}
