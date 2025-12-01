import java.util.*;
public class BillingSystem
{
    static Scanner console = new Scanner (System.in);
    public static void main (String[]args)
    {
        login();

        int choice;
        char ch;
        do
        {
            System.out.println("------Main Menu------");
            System.out.println("1. Inventory");
            System.out.println("2. Billing");
            System.out.println("3. Return/Logout");
            System.out.println("Enter your choice (1-3):");
            choice = console.nextInt();

            switch(choice)
            {
                case 1: 
                {
                    inventory();
                    break;
                }
                case 2:
                {
                    billing();
                    break;
                }
                case 3:
                {
                    login();
                    break;
                }
                default:
                {
                    System.out.println("Invalid Input!");
                }
            }

            System.out.println("Do you want to continue? (y/n)");
            ch = console.next().charAt(0);
        }while(ch!='n');

        console.close();
    }

    public static void login()
    {
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

            if (id.equals(SYSTEM_ID) && pass.equals(SYSTEM_PASSWORD)) {
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
            System.exit(0); // closes the program
        }
    }
    
    public static void inventory()
    {
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
            choice = console.nextInt();

            switch (choice) {
                case 1: System.out.println("Add new product here..."); 
			break;
                case 2: System.out.println("Price alteration here..."); 
			break;
                case 3: System.out.println("View all items here..."); 
			break;
                case 4: System.out.println("Restocking here..."); 
			break;
                case 5: System.out.println("Returning to Main Menu..."); 
                return;
                case 6: System.out.println("Exiting system..."); 
			    System.exit(0);
                default: System.out.println("Invalid choice.");
            }

        }while (choice != 5);
    
    }

    public static void billing()
    {
        System.out.println("------Billing Menu------");
        int choice;
        char ch;
        do
        {
            System.out.println("1. Add to Cart");
            System.out.println("2. Calculating total");
            System.out.println("3. Enter coupon/discount code");
            System.out.println("4. Print Receipt");
            System.out.println("5. Return");
            System.out.println("6. Exit");
            System.out.println("Enter your choice (1-6)");
            choice = console.nextInt();
            
            switch(choice)
            {
                case 1: 
                {
                    addToCart(); //inside this call/check stock-shortage(warning)
                    break;
                }
                case 2:
                {
                    totalCalculation();
                    break;
                }
                case 3:
                {
                    discount();
                    break;
                }
                case 4:
                {
                    printReceipt(); // after printing receipt call updateStock()
                    break;
                }
                case 5:
                {
                    return;
                }
                case 6:
                {
                    System.out.println("System Exited!");
                    System.exit(0);
                }
                default:
                {
                    System.out.println("Invalid Input!");
                }
            }
            System.out.println("Do you want to continue in Billing Menu? (y/n)");
            ch = console.next().charAt(0);
        }while(ch!='n');
    }

    public static void addToCart()
    {

    }
    public static void totalCalculation()
    {

    }
    public static void discount()
    {

    }
    public static void printReceipt()
    {
        
    }
    
}