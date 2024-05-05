package com.pluralsight;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();
        double totalAmount = 0.0;

        loadInventory("products.csv", inventory);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 3) {
            System.out.println("Welcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    // Method for reading the product information from the CSV file and populate the inventory ArrayList with Product objects
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double price  = Double.parseDouble(parts[2]);
                    inventory.add(new Product(id, name, price));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method for displaying a list of products from the inventory
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        System.out.println("List of products: ");
        for (Product product : inventory) {
            System.out.println(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
        }
        System.out.println("Enter the ID of the product you want to add to your cart (or type 'back' to go back):");
        String input = scanner.nextLine();
        if (!input.equals("back")) {
            Product selectedProduct = findProductById(input, inventory);
            if (selectedProduct != null) {
                cart.add(selectedProduct);
                System.out.println(selectedProduct.getName() + " has been added to your cart.");
            } else {
                System.out.println("Product not found!");
            }
        }
        }

        // Method for displaying the items in the cart, along with the total cost of all items or remove items by entering the ID
    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        // This method should display the items in the cart ArrayList, along
        // with the total cost of all items in the cart. The method should
        // prompt the user to remove items from their cart by entering the ID
        // of the product they want to remove. The method should update the cart ArrayList and totalAmount
        // variable accordingly.
        System.out.println("Your cart: ");
        double cartTotal = 0.0;
        for (Product product : cart) {
            System.out.println(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
            cartTotal += product.getPrice();
        }
        System.out.println("Total: $" + cartTotal);
        System.out.println("Enter the ID of the product you want to remove from your cart (or type 'Back' to go back):");
        String input = scanner.nextLine();
        if (!input.equals("Back")) {
            Product selectedProduct = findProductById(input, cart);
            if (selectedProduct != null) {
                cart.remove(selectedProduct);
                System.out.println(selectedProduct.getName() + " has been removed from your cart.");
            } else {
                System.out.println("Product not found in your cart!");
            }
        }
        }

    public static void checkOut(ArrayList<Product> cart, double totalAmount) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding Product object. If
        // no product with the specified ID is found, the method should return
        // null.
    }
}





