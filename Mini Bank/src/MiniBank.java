import model.Account;
import service.BankService;

import java.nio.file.Path;
import java.util.*;

public class MiniBank {

    /** A menu action that may fail; lets us wrap every option in one try/catch. */
    interface Action { String run() throws Exception; }

    public static void main(String[] args) {
        BankService service = new BankService(Path.of("data"));
        Scanner sc = new Scanner(System.in);

        System.out.println(new BankInfo("MiniBank", "Anand Main Branch"));
        System.out.println(safely(service::loadState));

        MenuOption choice;
        do {
            printMenu();
            choice = readChoice(sc);

            String message = switch (choice) {
                case OPEN_ACCOUNT -> safely(() -> {
                    String name   = ask(sc, "Name: ");
                    String email  = ask(sc, "Email: ");
                    String mobile = ask(sc, "Mobile: ");
                    String type   = ask(sc, "Type (SAVINGS / CURRENT / FD): ");
                    long opening  = Long.parseLong(ask(sc, "Opening balance: "));
                    return "Account created: " + service.openAccount(name, email, mobile, type, opening);
                });
                case DEPOSIT -> safely(() -> {
                    String ac = ask(sc, "Account number: ");
                    long amt = Long.parseLong(ask(sc, "Amount: "));
                    service.deposit(ac, amt);
                    return "Deposited " + amt + ". New balance: " + service.find(ac).getBalance();
                });
                case WITHDRAW -> safely(() -> {
                    String ac = ask(sc, "Account number: ");
                    long amt = Long.parseLong(ask(sc, "Amount: "));
                    service.withdraw(ac, amt);
                    return "Withdrawn " + amt + ". New balance: " + service.find(ac).getBalance();
                });
                case TRANSFER -> safely(() -> {
                    String from = ask(sc, "From account: ");
                    String to   = ask(sc, "To account: ");
                    long amt = Long.parseLong(ask(sc, "Amount: "));
                    service.transfer(from, to, amt);
                    return "Transferred " + amt + " from " + from + " to " + to;
                });
                case STATEMENT -> safely(() -> service.statement(ask(sc, "Account number: ")));
                case LIST_ACCOUNTS -> safely(() -> describe(service.listAccounts()));
                case SEARCH_BY_OWNER -> safely(() -> describe(service.searchByOwner(ask(sc, "Owner name contains: "))));
                case BATCH_PROCESS -> safely(() -> {
                    System.out.println("Enter commands like: DEPOSIT AC0001 500  (blank line to finish)");
                    List<String> lines = new ArrayList<>();
                    String line;
                    while (!(line = ask(sc, "> ")).isEmpty()) lines.add(line);
                    return String.join("\n", service.runBatch(lines));
                });
                case REPORT -> safely(service::report);
                case DEMOS -> safely(() -> {
                    System.out.println("""
                            1  Polymorphism & interest      (Practical 5/6)
                            2  equals / toString / clone    (Practical 3)
                            3  Validators & parser          (Practical 4)
                            4  Anonymous class vs lambda    (Practical 6)
                            5  Annotations + reflection     (Practical 7)
                            6  Exceptions & try-with-res    (Practical 8)
                            7  Race condition & fix         (Practical 9)
                            8  Thread pool, wait/notify, deadlock (Practical 10)
                            9  Save / load / report         (Practical 11)
                            10 Collections & generics       (Practical 12/13)""");
                    return Demos.run(Integer.parseInt(ask(sc, "Demo number: ")));
                });
                case EXIT -> safely(() -> service.saveState() + "\nGoodbye! Thank you for using MiniBank.");
            };
            System.out.println(message);
        } while (choice != MenuOption.EXIT);
    }

    // ---------- small helpers ----------
    private static void printMenu() {
        System.out.println("\n===== MENU =====");
        MenuOption[] options = MenuOption.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    private static MenuOption readChoice(Scanner sc) {
        MenuOption[] options = MenuOption.values();
        while (true) {
            System.out.print("Choose option: ");
            if (!sc.hasNextLine()) return MenuOption.EXIT;           // input ended
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= 1 && n <= options.length) return options[n - 1];
            } catch (NumberFormatException ignored) { }
            System.out.println("Invalid choice. Enter a number from 1 to " + options.length + ".");
        }
    }

    private static String ask(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static String describe(List<Account> list) {
        if (list.isEmpty()) return "(no accounts)";
        StringBuilder sb = new StringBuilder();
        for (Account a : list) sb.append(a).append("\n");
        return sb.toString().trim();
    }

    private static String safely(Action action) {
        try {
            return action.run();
        } catch (exception.BankException e) {
            return "Failed: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Invalid input: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e;
        } finally {
            // runs after every menu action
        }
    }
}
