/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author HS
 */
public class OrderItem {
    private int orderId;
    private int productId;
    private int quantity;
    private double sellPrice;

    public OrderItem() {
    }

    public OrderItem(int orderId, int productId, int quantity, double sellPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }
    
    public String print(){
        String productName = ((Product)Manager.Products.search(productId)).getName();
        return String.format("%-40s|%20d|%30.2f", productName, quantity, sellPrice);
    }

    @Override
    public String toString() {
        return String.format("%-15d|%-15d|%20d|%30.2f", orderId, productId, quantity, sellPrice);
    }
    
}
