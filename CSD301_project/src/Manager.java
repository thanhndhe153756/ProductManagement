/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author HS
 */
public class Manager {

    protected static BTree Products = new BTree();
    protected static BTree Orders = new BTree();
    public static Scanner sc = new Scanner(System.in);
    public static SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat fd = new SimpleDateFormat("ddHHmmss");

    public Manager() {
    }

//      Product manager
    public Product inputProduct(int id) {
        String name = inputString("Name: ");
        double price = inputDouble("Price: ", 0, Double.POSITIVE_INFINITY);
        int stockQuantity = inputInt("Stock quantity: ", 0, Integer.MAX_VALUE);
        return new Product(id, name, price, stockQuantity);
    }

    public void addProduct() {
        System.out.println("\t\t\tAdd a product");
        int id;
        do {
            id = inputInt("ID: ", 0, Integer.MAX_VALUE);
            if (Products.search(id) == null) {
                break;
            }
            System.out.println("Product ID existed!");
        } while (true);

        Products.add(id, inputProduct(id));
        saveProductFile();
        System.out.println("Add product successfully!");
    }

    public void addProduct(int id, String name, double price, int stockQuantity) {
        Products.add(id, new Product(id, name, price, stockQuantity));
        saveProductFile();
    }

    public void saveProductFile() {
        String s = String.format("%-10s|%-48s|%24s|%15s\n", "ID", "Name", "Price", "Stock").concat(Products.breadthFirstOrderString());
        saveTextFile("products.txt", s);
    }

    public void loadProductFile() {
        ArrayList<String[]> list = loadTextFile("products.txt");
        for (String[] s : list) {
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            double price = Double.parseDouble(s[2]);
            int stockQuantity = Integer.parseInt(s[3]);
            Products.add(id, new Product(id, name, price, stockQuantity));
        }
    }

    public void displayProduct() {
        System.out.println("\t\t\tDisplay product list");
        System.out.printf("%-10s|%-48s|%24s|%15s\n", "ID", "Name", "Price", "Stock");
        System.out.println(Products.toString());
    }

    public void updateProduct() {
        System.out.println("\t\t\tUpdate a product");
        int id = inputInt("ID: ", 0, Integer.MAX_VALUE);
        if (Products.search(id) == null) {
            System.out.println("ID does not exist");
        } else {
            Products.delete(id);
            Products.add(id, inputProduct(id));
            saveProductFile();
            System.out.println("Update successfully!");
        }
    }

    public void deleteProduct() {
        System.out.println("\t\t\tDelete a product");
        int id = inputInt("ID: ", 0, Integer.MAX_VALUE);
        if (Products.search(id) == null) {
            System.out.println("ID does not exist");
        } else {
            Products.delete(id);
            saveProductFile();
            System.out.println("Delete successfully!");
        }
    }

//      Orders manager
    public void createOrder() {
        System.out.println("\t\t\tCreate an order");
        int OrderId = Integer.parseInt(fd.format(System.currentTimeMillis()));
        Date date;
        try {
            date = f.parse(f.format(System.currentTimeMillis()));
        } catch (ParseException ex) {
            date = inputDate("Date: ");
        }
        String customerName = inputString("Customer name: ");
        double amount = 0;
        Order order = new Order(OrderId, date, customerName, amount);
        do {
            int productId = inputInt("Product ID: ", 0, Integer.MAX_VALUE);
            Product product = (Product) Products.search(productId);
            if (product == null) {
                System.out.println("Product ID does not exist");
                continue;
            }
            int stockQuatity = product.getStockQuantity();
            if (stockQuatity == 0) {
                System.out.println("Product is out of stock!");
                continue;
            }
            int quantity;
            do {
                quantity = inputInt("Quantity: ", 0, Integer.MAX_VALUE);
                if (quantity > stockQuatity) {
                    System.out.println("Quantity must smaller than stock quantity (" + stockQuatity + ")");
                } else {
                    product.setStockQuantity(stockQuatity - quantity);
                    break;
                }
            } while (true);
            double sellPrice = product.getPrice();
            amount += sellPrice * quantity;
            OrderItem orderItem = (OrderItem) order.getOrderItems().search(productId);
            if (orderItem != null) {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
            } else {
                orderItem = new OrderItem(OrderId, productId, quantity, sellPrice);
                order.getOrderItems().add(productId, orderItem);
            }
            if (inputString("Continue(y/n)? ").equalsIgnoreCase("n")) {
                break;
            }
        } while (true);
        order.setAmount(amount);
        Orders.add(OrderId, order);
        saveOrderFile();
        saveOrderItemFile();
        System.out.println("Create order successfully!");
    }

    public void saveOrderFile() {
        String s = String.format("%-10s|%-15s|%-48s|%24s\n", "ID", "Date", "Customer Name", "Amount").concat(Orders.breadthFirstOrderString());
        saveTextFile("orders.txt", s);
    }

    public void loadOrderFile() {
        ArrayList<String[]> list = loadTextFile("orders.txt");
        for (String[] s : list) {
            int id = Integer.parseInt(s[0]);
            Date date;
            try {
                date = f.parse(s[1]);
            } catch (Exception ex) {
                date = ((Order) Orders.search(id)).getDate();
            }
            String customerName = s[2];
            double amount = Double.parseDouble(s[3]);
            Orders.add(id, new Order(id, date, customerName, amount));
        }
    }

    public void saveOrderItemFile() {
        String s = String.format("%-15s|%-15s|%20s|%30s\n", "Order ID", "Product ID", "Quantity", "Sell Price");
        for (Integer orderId : Orders.getKeys()) {
            Order order = (Order) Orders.search(orderId);
            s = s.concat(order.orderItemToString());
        }
        saveTextFile("orderItems.txt", s);
    }

    public void loadOrderItemFile() {
        ArrayList<String[]> list = loadTextFile("orderItems.txt");
        for (String[] s : list) {
            int orderId = Integer.parseInt(s[0]);
            int productId = Integer.parseInt(s[1]);
            int quantity = Integer.parseInt(s[2]);
            double sellPrice = Double.parseDouble(s[3]);
            OrderItem orderItem = new OrderItem(orderId, productId, quantity, sellPrice);
            ((Order) Orders.search(orderId)).getOrderItems().add(productId, orderItem);
        }
    }

    public void displayOrders() {
        System.out.println("\t\t\tOrder list");
        System.out.printf("%-10s|%-15s|%-48s|%24s\n", "ID", "Date", "Customer Name", "Amount");
        System.out.println(Orders.toString());
    }

    public void displayOrdersInDetail() {
        System.out.println("\t\t\tOrder list");
//        System.out.printf("%-15s%-30s%-30s%30s\n","ID","Date","Customer Name","Amount");
//        System.out.println(Orders.toString());
        for (Integer orderId : Orders.getKeys()) {
            orderDetail(orderId);
        }
    }

    public void displayOrderInDetail() {
        System.out.println("\t\t\tOrder detail");
        int orderId = inputInt("Order ID: ", 0, Integer.MAX_VALUE);
        if (Orders.search(orderId) == null) {
            System.out.println("ID does not exist");
        } else {
            orderDetail(orderId);
        }
    }

    public void orderDetail(int orderId) {
        Order order = (Order) Orders.search(orderId);
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("Order ID: %-15s Customer name: %-42s Date: %10s\n", orderId, order.getCustomerName(), Manager.f.format(order.getDate()));
        System.out.printf("\n%-7s|%-40s|%20s|%30s\n", "No", "Product name", "Quantity", "Price");
        int i = 1;
        for (Integer key : order.getOrderItems().getKeys()) {
            OrderItem orderItem = (OrderItem) order.getOrderItems().search(key);
            System.out.printf("%-7d|%s\n", i++, orderItem.print());
        }
        System.out.printf("Amount: %92.2f\n", order.getAmount());
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public void bftrarvese() {
//        Products.breadthFirstOrder();
        System.out.println(Products.breadthFirstOrderString());
    }

//      Create data
    public void createData() {
        loadProductFile();
        loadOrderFile();
        loadOrderItemFile();
        saveProductFile();
        saveOrderFile();
        saveOrderItemFile();
    }

//      Read and Write files
    void saveTextFile(String fname, String s) {
        try {
            File f = new File(fname);
            FileWriter fw = new FileWriter(f);
            fw.write(s);
            fw.close();
        } catch (IOException ex) {
            System.out.println("Error while reading file: " + ex);
        }
    }

    ArrayList<String[]> loadTextFile(String fname) {
        ArrayList<String[]> list = new ArrayList<>();
        try {
            File f = new File(fname);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String line;
            String[] s;
            while ((line = br.readLine()) != null) {
                s = line.split("[|]");
                for (int i = 0; i < s.length; i++) {
                    s[i] = s[i].trim();
                }
                list.add(s);
            }
            fr.close();
            br.close();
        } catch (Exception ex) {
            System.out.println("Error while writing file: " + ex);
        }
        return list;
    }

//      Check input
    public static int inputInt(String msg, int min, int max) {
        int a;
        do {
            try {
                System.out.print(msg);
                a = Integer.parseInt(sc.nextLine());
                if (a >= min && a <= max) {
                    return a;
                } else {
                    System.out.println("You must enter number from " + min + " to " + max);
                }
            } catch (Exception e) {
                System.out.println("Accept number only");
            }
        } while (true);
    }

    public static double inputDouble(String msg, double min, double max) {
        double a;
        do {
            try {
                System.out.print(msg);
                a = Double.parseDouble(sc.nextLine());
                if (a >= min && a <= max) {
                    return a;
                } else {
                    System.out.println("You must enter number from " + min + " to " + max);
                }
            } catch (Exception e) {
                System.out.println("Accept number only");
            }
        } while (true);
    }

    public static String inputString(String msg) {
        String str;
        do {
            System.out.print(msg);
            str = sc.nextLine();
            if (str.isEmpty()) {
                System.out.println("This field cannot be empty");
            } else {
                return str;
            }
        } while (true);
    }

    public static Date inputDate(String message) {
        f.setLenient(false);
        Date d;
        do {
            try {
                System.out.print(message);
                d = f.parse(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Date must be in the format dd-MM-yyyy");
            }
        } while (true);
        return d;
    }
}
