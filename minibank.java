import java.util.Scanner;

record BankInfo(String name,String branch){}

enum MenuOption{
    OPEN_ACCOUNT,
    WITHDRAW,
    DEPOSIT,
    TRANSFER,
    EXIT
}

class Customer{

    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    private static long customerCounter=100;

    private static String generateCustomerId(){
        customerCounter++;
        return "CUST"+customerCounter;

    }

    public Customer(String name,String email,String mobile){
        this.customerId=generateCustomerId();
        this.name=name;
        this.email=email;
        this.mobile=mobile;
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getMobile(){
        return mobile;
    }

}

class Account{

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter=0;

    private static String generateAccountNumber(){
        accountCounter++;
        return String.format("AC%04d",accountCounter);
    }

    public Account(String ownerName,long balance){
        this.accountNumber=generateAccountNumber();
        this.ownerName=ownerName;
        this.balance=balance;
        this.active=true;
    }

    public Account(String ownerName){
        this(ownerName, 0);
    }

    public void deposit(long amount){
        balance+=amount;
    }

    public boolean withdraw(long amount){
        if(balance>=amount){
            balance-=amount;
            return true;
        }
        return false;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public long getBalance(){
        return balance;
    }

    public boolean isActive(){
        return active;
    }
}

public class minibank {
    
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);

        BankInfo bank=new BankInfo("MiniBank","CHARUSAT");
        System.out.println(bank);

        while(true){
            System.out.println("\n1. Open account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.println("Enter your choice:");

            int choice=sc.nextInt();

            MenuOption option;

            switch(choice){
                case 1 -> option=MenuOption.OPEN_ACCOUNT;
                case 2 -> option=MenuOption.DEPOSIT;
                case 3 -> option=MenuOption.WITHDRAW;
                case 4 -> option=MenuOption.TRANSFER;
                case 5 -> option=MenuOption.EXIT;
                default ->{
                    System.out.println("Invalid choice.");
                    continue;
                }
            }

            String message=switch(option){
                case OPEN_ACCOUNT -> 
                "Open account - to be implemented in later lab";
                case DEPOSIT ->
                "Deposit - to e implemented in later lab";
                case WITHDRAW ->
                "Withdraw - to be implemented in later lab";
                case TRANSFER ->
                "Transfer - to be implemented in later lab";
                case EXIT -> 
                "Goodbye!";
            };

            System.out.println(message);

            if(option==MenuOption.EXIT){
                break;
            }
        }

        sc.close();
    }
}
