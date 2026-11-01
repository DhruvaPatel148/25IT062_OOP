package service;

import exception.*;
import model.Account;
import util.Command;
import util.TransactionLog;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

/** Processes a batch of commands on a pool of 4 threads. Create a new one for each batch. */
public class TransactionProcessor {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<String> results = Collections.synchronizedList(new ArrayList<>());
    private final Path logFile;                    // may be null

    public TransactionProcessor(Path logFile) { this.logFile = logFile; }

    public void register(Account account) { accounts.put(account.getAccountNumber(), account); }

    public void submit(Runnable task) { pool.execute(task); }

    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) pool.shutdownNow();
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /** Runs all commands in parallel, waits for them and returns one result line per command. */
    public List<String> runBatch(List<Command> commands) {
        for (Command c : commands) submit(() -> execute(c));
        stop();
        return new ArrayList<>(results);
    }

    private void execute(Command c) {
        try {
            Account a = accounts.get(c.accountNumber());
            if (a == null) throw new AccountNotFoundException(c.accountNumber());
            switch (c.type()) {
                case DEPOSIT  -> a.deposit(c.amount());
                case WITHDRAW -> a.withdraw(c.amount());
                case TRANSFER -> throw new InvalidAmountException("TRANSFER is not supported in batch mode");
            }
            if (logFile != null) {
                TransactionLog.append(logFile, c.type() + " " + c.accountNumber() + " " + c.amount());
            }
            results.add("OK    " + c.type() + " " + c.accountNumber() + " " + c.amount()
                    + "  [" + Thread.currentThread().getName() + "]");
        } catch (Exception e) {
            results.add("FAIL  " + c.type() + " " + c.accountNumber() + " " + c.amount() + " -> " + e.getMessage());
        }
    }
}
