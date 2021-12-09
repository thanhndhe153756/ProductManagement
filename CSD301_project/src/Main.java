/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author HS
 */
public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        m.createData();
        int choice;
        do {            
            System.out.println("========================================= Store management =========================================");
            System.out.println("1. Add a product");
            System.out.println("2. Display products");
            System.out.println("3. Update product");
            System.out.println("4. Delete product");
            System.out.println("5. Create an order");
            System.out.println("6. Display orders");
            System.out.println("7. Display orders in detail");
            System.out.println("8. Display an order");
            System.out.println("0. Exit");
            choice = Manager.inputInt("Enter your choice: ",0,8);
            switch(choice){
                case 1:
                    m.addProduct();
                    break;
                case 2:
                    m.displayProduct();
                    break;
                case 3:
                    m.updateProduct();
                    break;
                case 4:
                    m.deleteProduct();
                    break;
                case 5:
                    m.createOrder();
                    break;
                case 6:
                    m.displayOrders();
                    break;
                case 7:
                    m.displayOrdersInDetail();
                    break;
                case 8:
                    m.displayOrderInDetail();
                    break;
                case 0:
                    break;
            }
        } while (choice != 0);
    }
}
