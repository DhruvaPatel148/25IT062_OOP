package service;

import exception.*;
import model.*;
import util.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static util.Validator.*;                    // the one static import (P6)

public class BankService {
    private final Repository<Account> accounts = new Repository<>();
    private final Repository<Customer> customers = new Repository<>();   // kept in memory only
    private final Path accountsFile, logsDir, transactionLog, reportFile;

    public BankService(Path dataDir) {
        this.accountsFile = dataDir.resolve("accounts.dat");
        this.logsDir = dataDir.resolve("logs");
        this.transactionLog = logsDir.resolve("transactions.log");
        this.reportFile = dataDir.resolve("report.txt");
    }

    // ---------- operations used by the menu ----------
    public Account openAccount(String name, String email, String mobile, String type, long opening) {
        if (name == null || name.isBlank())  throw new IllegalArgumentException("Name is required");
        if (!isValidEmail(email))            throw new IllegalArgumentException("Invalid email");
        if (!isValidMobile(mobile))          throw new IllegalArgumentException("Invalid mobile (10 digits, starts with 6-9)");
        if (opening < 0)                     throw new IllegalArgumentException("Opening balance cannot be negative");

        Account account = switch (type.toUpperCase()) {
            case "SAVINGS" -> {
                if (opening < 500) throw new IllegalArgumentException("Savings needs at least 500 to open");
                yield new SavingsAccount(name, opening, 500);
            }
            case "CURRENT" -> new CurrentAccount(name, opening, 5000);
            case "FD" -> {
                if (opening <= 0) throw new IllegalArgumentException("Fixed deposit needs an amount");
                yield new FixedDepositAccount(name, opening);
            }
            default -> throw new IllegalArgumentException("Type must be SAVINGS, CURRENT or FD");
        };
        customers.save(new Customer(name, email, mobile));
        accounts.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException, IOException {
        find(accountNumber).deposit(amount);
        TransactionLog.append(transactionLog, "DEPOSIT " + accountNumber + " " + amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException, IOException {
        find(accountNumber).withdraw(amount);
        TransactionLog.append(transactionLog, "WITHDRAW " + accountNumber + " " + amount);
    }

    public void transfer(String from, String to, long amount) throws BankException, IOException {
        find(from).transfer(find(to), amount);
        TransactionLog.append(transactionLog, "TRANSFER " + from + "->" + to + " " + amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(find(accountNumber));
    }

    public List<Account> listAccounts() {
        List<Account> list = accounts.findAll();
        Collections.sort(list);                          // Comparable: by account number
        return list;
    }

    public List<Account> searchByOwner(String text) {
        List<Account> found = new ArrayList<>();
        for (Account a : listAccounts()) {
            if (a.getOwnerName().toLowerCase().contains(text.toLowerCase())) found.add(a);
        }
        return found;
    }

    // ---------- batch processing on the thread pool ----------
    public List<String> runBatch(List<String> lines) {
        TransactionProcessor processor = new TransactionProcessor(transactionLog);
        for (Account a : accounts.findAll()) processor.register(a);

        List<String> output = new ArrayList<>();
        List<Command> commands = new ArrayList<>();
        for (String line : lines) {
            try {
                commands.add(CommandParser.parse(line));
            } catch (IllegalArgumentException e) {
                output.add("BAD   \"" + line + "\" -> " + e.getMessage());
            }
        }
        output.addAll(processor.runBatch(commands));
        return output;
    }

    // ---------- persistence and report ----------
    public String saveState() throws IOException {
        Account[] all = accounts.findAll().toArray(new Account[0]);
        StatePersister.save(all, accountsFile);
        return "Saved " + all.length + " account(s).";
    }

    public String loadState() throws IOException, ClassNotFoundException {
        if (!Files.exists(accountsFile)) return "No saved data yet - starting fresh.";
        Account[] loaded = StatePersister.load(accountsFile);
        for (Account a : loaded) accounts.save(a);
        return "Loaded " + loaded.length + " account(s) from disk.";
    }

    public String report() throws IOException {
        return ReportGenerator.generate(logsDir, reportFile);
    }

    // ---------- helper ----------
    public Account find(String accountNumber) throws AccountNotFoundException {
        Account a = accounts.findById(accountNumber.trim().toUpperCase());
        if (a == null) throw new AccountNotFoundException(accountNumber);
        return a;
    }
}
