import java.io.*;
import java.util.*;

public class BillingSystem{
    static Scanner console = new Scanner(System.in);
    static final String INVENTORY_FILE = "inventory.txt";
    static int nextProductId = 100;
    static double subTotal = 0.0;
    static String[] cartItems = new String[100];
    static int[] cartQuantities = new int[100];
    static double[] cartPrices = new double[100];
    static int cartCount = 0;
    static double discountPercent = 0.0;
    static double finalTotal = 0.0;
    static double discountAmount = 0.0;
    static double gst = 0.0;
    static double otherTax = 0.0;
    static int receiptNumber = 1;

    public static void main(String[] args) {
        login();
        ensureInventoryFile();

        int choice;
        try {
            do {
                System.out.println("------Main Menu------");
                System.out.println("1. Inventory");
                System.out.println("2. Billing");
                System.out.println("3. Return/Logout");
                System.out.println("4. Exit System");
                System.out.print("Enter your choice (1-4): ");

                try {
                    choice = console.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter 1, 2, 3, or 4.");
                    console.next(); // clear buffer
                    choice = 0;
                    continue;
                }

                switch (choice) {
                    case 1 -> inventory();
                    case 2 -> billing();
                    case 3 -> login();
                    case 4 -> {
                        System.out.println("Exiting program. Goodbye!");
                        console.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid Input!");
                }
            } while (true);
        } catch (NoSuchElementException e) {
            System.out.println("No input detected. Exiting program.");
            System.exit(0);
        }
    }

    // === LOGIN ===
    public static void login() {
        final String SYSTEM_ID = "admin";
        final String SYSTEM_PASSWORD = "1234";
        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < 3) {
            System.out.println("\n==== Billing Management System Login ====");
            System.out.print("Enter Login ID: ");
            String id = console.next();
            System.out.print("Enter Password: ");
            String pass = console.next();

            if (id.equalsIgnoreCase(SYSTEM_ID) && pass.equals(SYSTEM_PASSWORD)) {
                System.out.println("\nLogin Successful!");
                loggedIn = true;
                break;
            } else {
                attempts++;
                System.out.println("\nIncorrect ID or Password. Attempts left: " + (3 - attempts));
            }
        }

        if (!loggedIn) {
            System.out.println("\n3 unsuccessful attempts. System locked.");
            System.out.println("Exiting program...");
            System.exit(0);
        }
    }

    // === INVENTORY FILE ===
    public static void ensureInventoryFile() {
        try {
            File file = new File(INVENTORY_FILE);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("inventory.txt created automatically.");
            }
        } catch (IOException e) {
            System.out.println("Failed to create inventory file.");
        }
    }

    // === INVENTORY MENU ===
    public static void inventory() {
        int choice;
        do {
            System.out.println("\n=== Inventory Menu ===");
            System.out.println("1. Entry for New Product");
            System.out.println("2. Price Alteration");
            System.out.println("3. View All Items");
            System.out.println("4. Restocking");
            System.out.println("5. Return to Main Menu");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice! Please enter a number 1-6.");
                console.next();
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1 -> addNewProduct();
                case 2 -> alterPrice();
                case 3 -> stockList();
                case 4 -> restock();
                case 5 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                case 6 -> {
                    System.out.println("Exiting system...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (true);
    }

    public static void addNewProduct() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE, true))) {
            int id = ++nextProductId;
            System.out.print("Enter product name: ");
            String name = console.next();

            double price = -1;
            while (price < 0) {
                System.out.print("Enter price: ");
                try {
                    price = console.nextDouble();
                    if (price < 0) System.out.println("Price cannot be negative.");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    console.next();
                }
            }

            int stock = -1;
            while (stock < 0) {
                System.out.print("Enter stock quantity: ");
                try {
                    stock = console.nextInt();
                    if (stock < 0) System.out.println("Stock cannot be negative.");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number.");
                    console.next();
                }
            }

            System.out.print("Enter category: ");
            String category = console.next();
            writer.println(id + "," + name + "," + price + "," + stock + "," + category);
            System.out.println("Product added successfully! ID: " + id);
        } catch (IOException e) {
            System.out.println("Error writing inventory file: " + e.getMessage());
        }
    }

    public static void alterPrice() {
        System.out.print("Enter Product Name: "); // Changed from ID to Name
        String key = console.next();
        double newPrice = -1;
        while (newPrice < 0) {
            System.out.print("Enter new price: ");
            try {
                newPrice = console.nextDouble();
                if (newPrice < 0) System.out.println("Price cannot be negative.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                console.next();
            }
        }
        updateInventoryField(key, newPrice, -1);
    }

    public static void restock() {
        System.out.print("Enter Product Name: "); // Changed from ID to Name
        String key = console.next();
        int qty = 0;
        while (qty <= 0) {
            System.out.print("Enter quantity to add: ");
            try {
                qty = console.nextInt();
                if (qty <= 0) System.out.println("Quantity must be positive.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                console.next();
            }
        }
        updateInventoryField(key, -1, qty);
    }
    public static void updateInventoryField(String key, double newPrice, int addStock) {
        try {
            File inputFile = new File(INVENTORY_FILE);
            File tempFile = new File("temp.txt");
            Scanner sc = new Scanner(inputFile);
            PrintWriter pw = new PrintWriter(tempFile);
            boolean found = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    pw.println(line);
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < 4) {
                    pw.println(line);
                    continue;
                }
                // Changed: Now compares key with Product Name (p[1])
                if (p[1].equalsIgnoreCase(key)) {
                    found = true;
                    double price = Double.parseDouble(p[2]);
                    int stock = Integer.parseInt(p[3]);
                    if (newPrice >= 0) price = newPrice;
                    if (addStock > 0) stock += addStock;
                    pw.println(p[0] + "," + p[1] + "," + price + "," + stock);
                } else {
                    pw.println(line);
                }
            }
            sc.close();
            pw.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
            if (found) System.out.println("Inventory updated successfully.");
            else System.out.println("Product not found.");
        } catch (IOException e) {
            System.out.println("Error updating inventory.");
        }
    }

    // === BILLING FUNCTIONS ===
    public static void billing() {
        int choice;
        do {
            System.out.println("------Billing Menu------");
            System.out.println("1. Add to Cart");
            System.out.println("2. Enter coupon/discount code");
            System.out.println("3. Calculating total");
            System.out.println("4. Print Receipt");
            System.out.println("5. Return");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            try {
                choice = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number 1-6.");
                console.next();
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1 -> addToCart();
                case 2 -> discount();
                case 3 -> totalCalculation();
                case 4 -> printReceipt();
                case 5 -> { return; }
                case 6 -> {
                    System.out.println("System Exited!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid Input!");
            }
        } while (true);
    }

    public static void addToCart() {
        int choice;
        do {
            System.out.println("---Add to Cart---");
            System.out.println("1. View the entire stock");
            System.out.println("2. Enter Product Name and Quantity");
            System.out.println("3. View current cart");
            System.out.println("4. Remove item");
            System.out.println("5. Return to Billing Menu");
            System.out.print("Enter your choice (1-5): ");

            try { choice = console.nextInt(); } 
            catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number 1-5.");
                console.next();
                choice = 0;
                continue;
            }

            switch(choice) {
                case 1 -> stockList();
                case 2 -> enterProduct();
                case 3 -> viewCart();
                case 4 -> removeFromCart();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        } while(true);
    }

    public static void stockList() {
        System.out.println("-----Stock List-----");
        System.out.println("ID, Name, Price, Quantity, Category");
        System.out.println("----------------------");

        try {
            File f = new File(INVENTORY_FILE);
            Scanner fileReader = new Scanner(f);
            while(fileReader.hasNextLine()) {
                System.out.println(fileReader.nextLine());
            }
            fileReader.close();
        } catch(FileNotFoundException e) {
            System.out.println("Inventory file not found!");
        }
    }

    public static void enterProduct() {
    char ch = 'y';
    do {
        System.out.print("Enter Product Name (or type 'back' to return): ");
        String productName = console.next();
        if (productName.equalsIgnoreCase("back")) return;
        boolean found = false;
        try {
            File file = new File(INVENTORY_FILE);
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                if (parts[1].equalsIgnoreCase(productName)) {
                    found = true;
                    double price = Double.parseDouble(parts[2]);
                    int availableStock = Integer.parseInt(parts[3]);
                    System.out.println("Product Found: " + productName);
                    System.out.println("Price: $" + price);
                    System.out.println("Available in stock: " + availableStock);
                    if (availableStock == 0) {
                        System.out.println("Sorry, Out of Stock!");
                        break;
                    } else if (availableStock <= 5) {
                        System.out.println("Warning: Low stock! Only " + availableStock + " left.");
                    }
                    int quantity = 0;
                    boolean validQuantity = false;
                    while (!validQuantity) {
                        System.out.print("Enter quantity: ");
                        try {
                            quantity = console.nextInt();
                            validQuantity = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid quantity! Please enter a number.");
                            console.next();
                        }
                    }
                    if (quantity <= 0) 
		    System.out.println("Quantity must be positive!");
                    else if (quantity > availableStock) {
    			System.out.print("Only " + availableStock + " available. Add " + availableStock + " instead? (y/n): ");
    			char choice = console.next().charAt(0);
    		if (choice == 'y' || choice == 'Y') quantity = availableStock;
			}
		if (quantity > 0) {
    			addOrUpdateCart(productName, quantity, price);
		}

                    break;
                }
            }
            fileReader.close();
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        if(!found) System.out.println("Product not found!");
        while(true) {
            System.out.print("Add another product? (y/n): ");
            String input = console.next().toLowerCase();
            if(input.equals("y") || input.equals("n")) {
                ch = input.charAt(0);
                break;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    } while(ch == 'y');
}

    public static void addOrUpdateCart(String itemName, int quantity, double price) {
        try {
            File inputFile = new File(INVENTORY_FILE);
            File tempFile = new File("temp_inventory.txt");
            Scanner reader = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(tempFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) {
                    writer.println(line);
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    writer.println(line);
                    continue;
                }
                String productId = parts[0];
                String productName = parts[1];
                price = Double.parseDouble(parts[2]);
                int stock = Integer.parseInt(parts[3]);
                for (int i = 0; i < cartCount; i++) {
                    // Changed: Now matches cartItems[i] (Product Name)
                    if (cartItems[i].equalsIgnoreCase(productName)) {
                        stock -= cartQuantities[i];
                        break;
                    }
                }
                writer.println(productId + "," + productName + "," + price + "," + stock);
            }
            reader.close();
            writer.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
            System.out.println("Stock updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    public static void viewCart() {
        if(cartCount == 0) { System.out.println("Cart is empty!"); return; }

        System.out.println("==== CART ====");
        System.out.println("Item\tQty\tPrice");
        for(int i=0; i<cartCount; i++)
            System.out.println(cartItems[i] + "\t" + cartQuantities[i] + "\t$" + cartPrices[i]);
        System.out.println("SUBTOTAL: $" + subTotal);
    }

    public static void removeFromCart() {
        if(cartCount == 0) { System.out.println("Cart is empty."); return; }
        viewCart();

        System.out.print("Enter item number to remove: ");
        int index = -1;
        try { int itemNum = console.nextInt(); index = itemNum - 1; }
        catch (InputMismatchException e) { System.out.println("Invalid input!"); console.next(); return; }

        if(index < 0 || index >= cartCount) { System.out.println("Invalid item number."); return; }

        subTotal -= cartQuantities[index] * cartPrices[index];
        for(int i=index; i<cartCount-1; i++) {
            cartItems[i] = cartItems[i+1];
            cartQuantities[i] = cartQuantities[i+1];
            cartPrices[i] = cartPrices[i+1];
        }

        cartItems[cartCount-1] = null;
        cartQuantities[cartCount-1] = 0;
        cartPrices[cartCount-1] = 0.0;
        cartCount--;

        System.out.println("Item removed.");
        viewCart();
    }

    public static void discount() {
        System.out.print("Enter coupon code: ");
        String code = console.next();
        if(code.equalsIgnoreCase("SAVE10")) { discountPercent = 0.10; System.out.println("Discount Applied: 10%"); }
        else if(code.equalsIgnoreCase("WELCOME")) { discountPercent = 0.20; System.out.println("Discount Applied: 20%"); }
        else { discountPercent = 0.0; System.out.println("Invalid coupon code. No discount applied."); }
    }

    public static void totalCalculation() {
        if(subTotal == 0.0) { System.out.println("Cart is empty, nothing to calculate."); return; }

        final double gstRate = 0.13;
        final double otherTaxRate = 0.02;

        System.out.println("\n----Bill Calculation----");
        System.out.println("Sub Total: $" + subTotal);
        discountAmount = subTotal * discountPercent;
        if(discountPercent > 0) System.out.println("Discount: -$" + discountAmount);

        double priceAfterDiscount = subTotal - discountAmount;
        gst = priceAfterDiscount * gstRate;
        otherTax = priceAfterDiscount * otherTaxRate;
        finalTotal = priceAfterDiscount + gst + otherTax;

        System.out.println("GST (13%): $" + gst);
        System.out.println("Other Tax (2%): $" + otherTax);
        System.out.println("Final Total: $" + finalTotal);
    }
public static void printReceipt() {
    if (cartCount == 0) {
        System.out.println("Cart is empty. Nothing to print.");
        return;
    }

    // Calculate total if not already done
    if (finalTotal == 0.0) {
        totalCalculation();
    }

    System.out.println();
    System.out.println("===================================");
    System.out.println("         GROCERY STORE RECEIPT      ");
    System.out.println("===================================");
    System.out.printf("%-15s %-5s %-10s %-10s\n", "Item", "Qty", "Price", "Total");
    System.out.println("-----------------------------------");

    for (int i = 0; i < cartCount; i++) {
        double itemTotal = cartQuantities[i] * cartPrices[i];
        System.out.printf("%-15s %-5d $%-10.2f $%-10.2f\n", cartItems[i], cartQuantities[i], cartPrices[i], itemTotal);
    }

    System.out.println("-----------------------------------");
    System.out.printf("Subtotal: $%-10.2f\n", subTotal);
    System.out.printf("Discount (%.0f%%): -$%-10.2f\n", discountPercent * 100, discountAmount);
    System.out.printf("GST (13%%): $%-10.2f\n", gst);
    System.out.printf("Other Tax (2%%): $%-10.2f\n", otherTax);
    System.out.println("-----------------------------------");
    System.out.printf("FINAL TOTAL: $%-10.2f\n", finalTotal);
    System.out.println("===================================");
    System.out.println("Thank you for shopping with us!");
    System.out.println("===================================");

    updateStock();
    saveReceiptToFile();
    clearCart();
}
public static void updateStock() {
    try {
        File inputFile = new File(INVENTORY_FILE);
        File tempFile = new File("temp_inventory.txt");
        Scanner reader = new Scanner(inputFile);
        PrintWriter writer = new PrintWriter(tempFile);

        while (reader.hasNextLine()) {
            String line = reader.nextLine().trim();
            if (line.isEmpty()) {
                writer.println(line);
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 4) {
                writer.println(line);
                continue;
            }

            String productId = parts[0];
            String productName = parts[1];
            double price = Double.parseDouble(parts[2]);
            int stock = Integer.parseInt(parts[3]);

            for (int i = 0; i < cartCount; i++) {
                if (cartItems[i].equalsIgnoreCase(productName)) {
                    stock -= cartQuantities[i];
                    break;
                }
            }

            writer.println(productId + "," + productName + "," + price + "," + stock);
        }

        reader.close();
        writer.close();
        inputFile.delete();
        tempFile.renameTo(inputFile);

        System.out.println("Stock updated successfully.");
    } catch (IOException e) {
        System.out.println("Error updating stock: " + e.getMessage());
    }
}

public static void saveReceiptToFile() {
    if (cartCount == 0) return;

    try {
        File receiptFile = new File("receipt_" + receiptNumber + ".txt");
        receiptNumber++;
        PrintWriter writer = new PrintWriter(receiptFile);

        writer.println("===================================");
        writer.println("         GROCERY STORE RECEIPT      ");
        writer.println("===================================");
        writer.printf("%-15s %-5s %-10s %-10s\n", "Item", "Qty", "Price", "Total");
        writer.println("-----------------------------------");

        for (int i = 0; i < cartCount; i++) {
            double itemTotal = cartQuantities[i] * cartPrices[i];
            writer.printf("%-15s %-5d $%-10.2f $%-10.2f\n", cartItems[i], cartQuantities[i], cartPrices[i], itemTotal);
        }

        writer.println("-----------------------------------");
        writer.printf("Subtotal: $%-10.2f\n", subTotal);
        writer.printf("Discount (%.0f%%): -$%-10.2f\n", discountPercent * 100, discountAmount);
        writer.printf("GST (13%%): $%-10.2f\n", gst);
        writer.printf("Other Tax (2%%): $%-10.2f\n", otherTax);
        writer.println("-----------------------------------");
        writer.printf("FINAL TOTAL: $%-10.2f\n", finalTotal);
        writer.println("===================================");
        writer.println("Thank you for shopping with us!");
        writer.println("===================================");

        writer.close();
        System.out.println("Receipt saved as: " + receiptFile.getName());
    } catch (IOException e) {
        System.out.println("Error saving receipt: " + e.getMessage());
    }
}

public static void clearCart() {
    for (int i = 0; i < cartCount; i++) {
        cartItems[i] = null;
        cartQuantities[i] = 0;
        cartPrices[i] = 0.0;
    }
    cartCount = 0;
    subTotal = 0.0;
    discountPercent = 0.0;
    discountAmount = 0.0;
    gst = 0.0;
    otherTax = 0.0;
    finalTotal = 0.0;
}
}