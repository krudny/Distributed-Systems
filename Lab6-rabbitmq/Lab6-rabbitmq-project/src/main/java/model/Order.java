package model;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Map<String, AtomicInteger> teamTotalOrderCounters = new HashMap<>();

    private String teamName;
    private String item;
    private int orderNumber;

    public Order(String teamName, String item) {
        this.teamName = teamName;
        this.item = item;
        this.orderNumber = getNextOrderNumber(teamName);
    }

    private static synchronized int getNextOrderNumber(String teamName) {
        teamTotalOrderCounters.putIfAbsent(teamName, new AtomicInteger(0));
        return teamTotalOrderCounters.get(teamName).incrementAndGet();
    }
}