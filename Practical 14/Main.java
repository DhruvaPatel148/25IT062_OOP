import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ItemService service = new ItemService();

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Show Count");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                service.addItem(name);
                System.out.println("Item added");
            } else if (choice == 2) {
                System.out.println("Count: " + service.getCount());
            } else if (choice == 3) {
                break;
            } else {
                throw new IllegalArgumentException("Invalid choice");
            }
        }

        sc.close();
    }
}