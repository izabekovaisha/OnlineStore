package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        scanner.close();
    }


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
                    inventory.add(new Product(id, name, price, 0));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        System.out.println("Products: ");
        for (Product product : inventory) {
            System.out.println(product.getId() + "|" + product.getName() + "|$" + product.getPrice());
        }

        System.out.println("Enter the ID of the product you want to add to your cart (or type 'Back' to go back):");
        String input = scanner.nextLine();

        if (!input.equals("Back")) {
            Product product;
            product = findProductById(input, inventory);
            if (product != null) {
                boolean found = false;
                for (Product cartProduct : cart) {
                    if (cartProduct.getId().equals(product.getId())) {
                        cartProduct.incrementQuantity();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cart.add(new Product(product.getId(), product.getName(), product.getPrice(), 1));
                }
                System.out.println(product.getName() + " has been added to your cart.");
            } else {
                System.out.println("Product not found!");
            }
        }
    }


    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        while (true) {
            if (cart.isEmpty()) {
                System.out.println("Your cart is empty.");
            } else {
                System.out.println("Your Cart: ");
                for (Product product : cart) {
                    System.out.println(product.getId() + ": " + product.getName() + " - $" + product.getPrice() + "x" + product.getQuantity());
                    totalAmount += product.getPrice() * product.getQuantity();
                }
            }

            System.out.println("Total: $" + totalAmount);
            System.out.println("1. Check Out");
            System.out.println("2. Remove Product from the cart");
            System.out.println("3. Clear Cart");
            System.out.println("4. Return to the home screen");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkOut(cart, totalAmount);
                    return;
                case 2:
                    System.out.println("Enter the ID of the product you want to remove from your cart (or type 'Back' to go back):");
                    String input = scanner.nextLine();
                    if (!input.equalsIgnoreCase("Back")) {
                        Product product = findProductById(input, cart);
                        if (product != null) {
                            cart.remove(product);
                            System.out.println(product.getName() + " has been removed from your cart.");
                            totalAmount = 0.0;
                        } else {
                            System.out.println("Product not found in your cart!");
                        }
                    }
                    break;
                case 3:
                    cart.clear();
                    System.out.println("Your cart has been cleared.");
                    break;
                case 4:
                    System.out.println("Returning to the home screen...");
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }


    public static void checkOut(ArrayList<Product> cart, double totalAmount) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Total amount to pay: $" + totalAmount);
        System.out.println("Enter the amount paid in cash:");
        double amountPaid = scanner.nextDouble();
        scanner.nextLine();

        if (amountPaid < totalAmount) {
            System.out.println("Payment not enough. Payment canceled.");
            return;
        }

        double change = amountPaid - totalAmount;
        System.out.println("Change given: " + change);

        // Display sales receipt and save to file
        String receiptInfo = generateReceiptInfo(cart, totalAmount, change);
        System.out.println(receiptInfo);
        saveReceiptToFile(receiptInfo);


        cart.clear();
    }


    public static String generateReceiptInfo(ArrayList<Product> cart, double totalAmount, double change) {
        StringBuilder receipt = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timestamp = now.format(formatter);

        String filename = timestamp + ".txt";

        receipt.append("Sales Receipt:\n");
        receipt.append("Order Date: ").append(now).append("\n");
        receipt.append("All Line Items:\n");
        for (Product product : cart) {
            double subtotal = product.getPrice() * product.getQuantity();
            receipt.append(product.getName()).append(" - $").append(product.getPrice()).append(" x ").append(product.getQuantity()).append(" = $").append(subtotal).append("\n");
        }
        receipt.append("Sales Total: $").append(totalAmount).append("\n");
        receipt.append("Amount Paid: $").append(totalAmount + change).append("\n");
        receipt.append("Change Given: $").append(change).append("\n");

        return receipt.toString();
    }


    public static void saveReceiptToFile(String receiptInfo) {
        try {
            File receiptsFolder = new File("Receipts");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            String timestamp = now.format(formatter);

            String filename = "Receipts/" + timestamp + ".txt";

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));

            bufferedWriter.write(receiptInfo);
            bufferedWriter.close();


            System.out.println("Sales receipt saved to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving receipt to file: " + e.getMessage());
        }
    }


    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}








