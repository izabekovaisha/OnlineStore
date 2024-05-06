package com.pluralsight;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {
        // Initialize variables
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();
        double totalAmount = 0.0;

        // Load inventory from CSV file
        loadInventory("products.csv", inventory);

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("Welcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            // Call the appropriate method based on user choice
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
                    double price = Double.parseDouble(parts[2]);
                    inventory.add(new Product(id, name, price));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    // Method for displaying a list of products from the inventory
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        // This method should display a list of products from the inventory,
        // and prompt the user to add items to their cart. The method should
        // prompt the user to enter the ID of the product they want to add to
        // their cart. The method should
        // add the selected product to the cart ArrayList.

        System.out.println("Products: ");
        for (Product product : inventory) {
            System.out.println(product.getId() + product.getName() + " - $" + product.getPrice());
        }

        // Prompt user to add items to cart
        System.out.println("Enter the ID of the product you want to add to your cart (or type 'Back' to go back):");
        String input = scanner.nextLine();

        if (!input.equals("Back")) {
            Product product = findProductById(input, inventory);
            if (product != null) {
                cart.add(product);
                System.out.println(product.getName() + " has been added to your cart.");
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
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Your Cart:");
            for (Product product : cart) {
                System.out.println(product.getId() + ": " + product.getName() + " - $" + product.getPrice());
            }
        }


        // Calculate total amount
        totalAmount = 0.0;
        for (Product product : cart) {
            totalAmount += product.getPrice();
        }
        System.out.println("Total: $" + totalAmount);

        // Prompt user to remove items from cart
        System.out.println("Enter the ID of the product you want to remove from your cart (or type 'Back' to go back):");
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("Back")) {
            Product product = findProductById(input, cart);
            if (product != null) {
                cart.remove(product);
                System.out.println(product.getName() + " has been removed from your cart.");
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
        System.out.println("Total amount to pay: $" + totalAmount);
        System.out.println("Confirm purchase? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("yes")) {
            System.out.println("Thank you for your purchase!");
            cart.clear();
        } else {
            System.out.println("Purchase canceled.");
        }
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding Product object. If
        // no product with the specified ID is found, the method should return
        // null.
        for (Product product : inventory) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
        }
    }








