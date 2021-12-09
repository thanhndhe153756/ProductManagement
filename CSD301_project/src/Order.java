/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;

/**
 *
 * @author HS
 */
public class Order {
    private int orderId;
    private Date date;
    private String customerName;
    private double amount;
    private BTree OrderItems = new BTree();

    public Order() {
    }

    public Order(int orderId, Date date, String customerName, double amount) {
        this.orderId = orderId;
        this.date = date;
        this.customerName = customerName;
        this.amount = amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BTree getOrderItems() {
        return OrderItems;
    }

    public void setOrderItems(BTree OrderItems) {
        this.OrderItems = OrderItems;
    }
    
    public String orderItemToString(){
        return OrderItems.toString();
    }

    @Override
    public String toString() {
        return String.format("%-10d|%-15s|%-48s|%24.2f", orderId, Manager.f.format(date), customerName, amount);
    }

}
