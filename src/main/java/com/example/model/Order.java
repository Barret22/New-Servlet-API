package com.example.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private LocalDateTime date;
    private double cost;
    private List<Product> products;

    public Order() {}

    public Order(int id, LocalDateTime date, double cost, List<Product> products) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.products = products;
    }

    // Геттери і сеттери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}