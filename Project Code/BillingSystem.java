import java.util.*;
public class BillingSystem
{
    static Scanner console = new Scanner (System.in);
    public static void main (String[]args)
    {
        System.out.println("======Billing System of a Grocery Store======");

        //Login menu called here
        //Login menu (should be a method so that it can be called in the main menu)

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
        System.out.println("------Login------");
        // eman's part 
    }
    
    public static void inventory()
    {
        System.out.println("------Inventory Menu------");
        //inventory method eman's part
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