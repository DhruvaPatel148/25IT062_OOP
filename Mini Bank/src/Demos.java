import exception.*;
import model.*;
import service.*;
import util.*;

import java.nio.file.*;
import java.util.*;

/** Small demonstrations of what each practical asked for. Reachable from the DEMOS menu option. */
public class Demos {

    public static String run(int n) throws Exception {
        switch (n) {
            case 1  -> polymorphism();
            case 2  -> objectMethods();
            case 3  -> validators();
            case 4  -> rules();
            case 5  -> annotations();
            case 6  -> exceptions();
            case 7  -> raceCondition();
            case 8  -> threadsAndDeadlock();
            case 9  -> persistence();
            case 10 -> collectionsAndGenerics();
            default -> throw new IllegalArgumentException("Choose 1-10");
        }
        return "(demo finished - note: demo accounts use up account numbers)";
    }

    // ---- Practical 5 / 6 ----
    static void polymorphism() {
        Account[] accounts = {
                new SavingsAccount("Riya", 5000, 500),
                new CurrentAccount("Amit", 2000, 5000),
                new FixedDepositAccount("Neha", 10000)
        };
        for (Account a : accounts) {                                  // run-time polymorphism
            System.out.println(a.getClass().getSimpleName() + " rate=" + a.interestRate()
                    + " yearlyInterest=" + a.yearlyInterest());
            if (a instanceof FixedDepositAccount fd) {                // pattern matching
                System.out.println("  locked deposit, can withdraw 100? " + fd.canWithdraw(100));
            }
            if (a instanceof Premium) System.out.println("  (premium account)");
        }
    }

    // ---- Practical 3 ----
    static void objectMethods() throws Exception {
        Account a1 = new SavingsAccount("Riya", 4000, 0);
        Path tmp = Files.createTempFile("acc", ".dat");
        StatePersister.save(new Account[]{a1}, tmp);
        Account a2 = StatePersister.load(tmp)[0];                     // different object, same number
        System.out.println("toString: " + a1);
        System.out.println("a1 == a2     : " + (a1 == a2));
        System.out.println("a1.equals(a2): " + a1.equals(a2));
        Object o = a1;
        System.out.println("o instanceof Account: " + (o instanceof Account));

        Customer c = new Customer("Riya", "riya@example.com", "9876543210",
                new Customer.Address("12 MG Road", "Anand", "388001"));
        Customer copy = c.clone();
        System.out.println("customer: " + c + " | city " + c.getAddress().getCity());
        System.out.println("clone same id: " + copy.getCustomerId().equals(c.getCustomerId())
                + ", different object: " + (copy != c) + ", different address object: " + (copy.getAddress() != c.getAddress()));
    }

    // ---- Practical 4 ----
    static void validators() {
        System.out.println("mobile 9876543210 -> " + Validator.isValidMobile("9876543210"));
        System.out.println("mobile 12345      -> " + Validator.isValidMobile("12345"));
        System.out.println("email abc@xyz.com -> " + Validator.isValidEmail("abc@xyz.com"));
        System.out.println("email abc@        -> " + Validator.isValidEmail("abc@"));
        System.out.println("PAN ABCDE1234F    -> " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN abcde1234f    -> " + Validator.isValidPan("abcde1234f"));
        System.out.println("IFSC SBIN0001234  -> " + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("IFSC SBIN1001234  -> " + Validator.isValidIfsc("SBIN1001234"));
        Command c = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("type=" + c.type() + ", accountNumber=" + c.accountNumber() + ", amount=" + c.amount());
        try {
            CommandParser.parse("DEPOSIT 500");
        } catch (IllegalArgumentException e) {
            System.out.println("bad line -> " + e.getMessage());
        }
    }

    // ---- Practical 6 ----
    static void rules() {
        Account acc = new SavingsAccount("Riya", 50000, 500);
        WithdrawRule anonymous = new WithdrawRule() {
            @Override public boolean allow(Account account, long amount) { return amount <= 10000; }
        };
        WithdrawRule lambda = (account, amount) -> amount <= 10000;
        for (long amt : new long[]{500, 20000}) {
            System.out.println("amount " + amt + " -> anonymous: " + anonymous.allow(acc, amt)
                    + ", lambda: " + lambda.allow(acc, amt));
        }
    }

    // ---- Practical 7 ----
    static void annotations() {
        Account bad = new SavingsAccount("Riya", -100, 0);
        Account good = new SavingsAccount("Amit", 100, 0);
        System.out.println("negative balance errors: " + Arrays.toString(AnnotationValidator.validate(bad)));
        System.out.println("valid account errors   : " + Arrays.toString(AnnotationValidator.validate(good)));
        Account longName = new SavingsAccount("A very very very long owner name indeed", 100, 0);
        System.out.println("long name errors       : " + Arrays.toString(AnnotationValidator.validate(longName)));
    }

    // ---- Practical 8 ----
    static void exceptions() {
        Account acc = new SavingsAccount("Riya", 1000, 0);
        try {
            acc.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage() + "  (shortfall field = " + e.getShortfall() + ")");
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("balance is still " + acc.getBalance());
        }
        try {
            acc.deposit(-5);
        } catch (InvalidAmountException e) {
            System.out.println("deposit rejected: " + e.getMessage());
        }
        System.out.println("try-with-resources:");
        try (Resource r = new Resource("DB connection")) {
            r.use(true);
        } catch (IllegalStateException e) {
            System.out.println("  caught after close: " + e.getMessage());
        }
    }

    // ---- Practical 9 ----
    /** Same as a SavingsAccount but deposit is NOT synchronized, so we can watch updates get lost. */
    static class RacyAccount extends SavingsAccount {
        private long raw;
        RacyAccount() { super("racy", 0, 0); }
        @Override public void deposit(long amount) {
            long old = raw;
            Thread.yield();                 // widen the window so the race shows up every run
            raw = old + amount;
        }
        @Override public long getBalance() { return raw; }
    }

    static void raceCondition() throws InterruptedException {
        int threads = 10, times = 1000;
        long expected = (long) threads * times;
        Account racy = new RacyAccount();
        runWorkers(racy, threads, times);
        System.out.println("WITHOUT synchronized: " + racy.getBalance() + " (expected " + expected + ")");
        Account safe = new SavingsAccount("safe", 0, 0);
        runWorkers(safe, threads, times);
        System.out.println("WITH synchronized   : " + safe.getBalance() + " (expected " + expected + ")");
    }

    private static void runWorkers(Account acc, int threads, int times) throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        for (int i = 1; i <= threads; i++) {
            Thread t = new Thread(new AccountWorker(acc, times, 1), "worker-" + i);
            if (i == 1) System.out.println(t.getName() + " state before start: " + t.getState());
            list.add(t);
            t.start();
        }
        for (Thread t : list) t.join();
        System.out.println(list.get(0).getName() + " state after join : " + list.get(0).getState());
    }

    // ---- Practical 10 ----
    static void threadsAndDeadlock() throws Exception {
        // 1) thread pool
        Account acc = new CurrentAccount("Pool", 0, 0);
        TransactionProcessor processor = new TransactionProcessor(null);
        processor.register(acc);
        List<Command> batch = new ArrayList<>();
        for (int i = 0; i < 12; i++) batch.add(new Command(TransactionType.DEPOSIT, acc.getAccountNumber(), 100));
        processor.runBatch(batch).stream().limit(4).forEach(System.out::println);
        System.out.println("  ... 12 deposits of 100 on a pool of 4 threads -> balance " + acc.getBalance());

        // 2) producer-consumer with wait/notify
        Buffer buffer = new Buffer();
        Account target = new CurrentAccount("PC", 0, 0);
        Thread producer = new Thread(() -> {
            try { for (long i = 1; i <= 5; i++) { buffer.put(i * 100); System.out.println("  produced " + i * 100); } }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "producer");
        Thread consumer = new Thread(() -> {
            try { for (int i = 0; i < 5; i++) { long v = buffer.take(); target.deposit(v); System.out.println("  consumed " + v); } }
            catch (Exception e) { System.out.println(e.getMessage()); }
        }, "consumer");
        producer.start(); consumer.start();
        producer.join(); consumer.join();
        System.out.println("  consumer deposited everything, balance " + target.getBalance() + " (expected 1500)");

        // 3) deadlock, then the fix
        Account a = new CurrentAccount("A", 1000, 0), b = new CurrentAccount("B", 1000, 0);
        Thread t1 = daemon(() -> naiveTransfer(a, b, 100));
        Thread t2 = daemon(() -> naiveTransfer(b, a, 100));
        t1.start(); t2.start();
        t1.join(1500); t2.join(1500);
        System.out.println("opposite lock order: " + ((t1.isAlive() || t2.isAlive()) ? "DEADLOCK (threads stuck)" : "finished"));

        Account c = new CurrentAccount("C", 1000, 0), d = new CurrentAccount("D", 1000, 0);
        Thread t3 = daemon(() -> safeTransfer(c, d, 100));
        Thread t4 = daemon(() -> safeTransfer(d, c, 50));
        t3.start(); t4.start();
        t3.join(1500); t4.join(1500);
        System.out.println("smaller number first: " + ((t3.isAlive() || t4.isAlive()) ? "stuck" : "finished")
                + " -> C=" + c.getBalance() + " D=" + d.getBalance() + " (expected 950 / 1050)");
    }

    private static Thread daemon(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);                 // so a stuck thread cannot keep the program alive
        return t;
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void naiveTransfer(Account from, Account to, long amount) {
        synchronized (from) {
            sleep(300);                    // give the other thread time to lock its first account
            synchronized (to) {
                try { from.withdraw(amount); to.deposit(amount); } catch (BankException e) { System.out.println(e.getMessage()); }
            }
        }
    }

    private static void safeTransfer(Account from, Account to, long amount) {
        sleep(100);
        try { from.transfer(to, amount); } catch (BankException e) { System.out.println(e.getMessage()); }
    }

    /** Small shared buffer for producer-consumer. */
    static class Buffer {
        private final long[] items = new long[3];
        private int count, in, out;

        synchronized void put(long v) throws InterruptedException {
            while (count == items.length) wait();          // full: wait for the consumer
            items[in] = v; in = (in + 1) % items.length; count++;
            notifyAll();
        }

        synchronized long take() throws InterruptedException {
            while (count == 0) wait();                     // empty: wait for the producer
            long v = items[out]; out = (out + 1) % items.length; count--;
            notifyAll();
            return v;
        }
    }

    // ---- Practical 11 ----
    static void persistence() throws Exception {
        Path dir = Files.createTempDirectory("minibank-demo");
        BankService first = new BankService(dir);
        Account a = first.openAccount("Riya", "riya@example.com", "9876543210", "SAVINGS", 5000);
        Account b = first.openAccount("Amit", "amit@example.com", "9123456780", "CURRENT", 2000);
        first.deposit(a.getAccountNumber(), 500);
        first.withdraw(b.getAccountNumber(), 200);
        System.out.println(first.saveState());

        BankService second = new BankService(dir);                 // pretend the app restarted
        System.out.println(second.loadState());
        second.listAccounts().forEach(x -> System.out.println("  reloaded: " + x));
        System.out.println(second.report());
    }

    // ---- Practical 12 / 13 ----
    static void collectionsAndGenerics() {
        AccountStore store = new AccountStore();
        store.add(new SavingsAccount("A", 5000, 0));
        store.add(new SavingsAccount("B", 2000, 0));
        store.add(new SavingsAccount("C", 9000, 0));
        System.out.println("sorted by balance (high to low):");
        store.sortedByBalance().forEach(a -> System.out.println("  " + a));
        System.out.println("balance above 3000: " + store.balanceAbove(3000).size() + " account(s)");

        Repository<Account> accountRepo = new Repository<>();
        accountRepo.saveAll(store.getAll().toArray(new Account[0]));
        String id = store.sortedByBalance().get(0).getAccountNumber();
        System.out.println("findById(" + id + ") -> " + accountRepo.findById(id));

        Repository<Customer> customerRepo = new Repository<>();       // same class, another type
        Customer c = customerRepo.save(new Customer("Riya", "riya@example.com", "9876543210"));
        System.out.println("customer repo -> " + customerRepo.findById(c.getCustomerId()) + " (count " + customerRepo.count() + ")");

        System.out.println("average Integer[]: " + Repository.average(new Integer[]{1, 2, 3, 4}));
        System.out.println("average Double[] : " + Repository.average(new Double[]{1.5, 2.5}));
    }
}
