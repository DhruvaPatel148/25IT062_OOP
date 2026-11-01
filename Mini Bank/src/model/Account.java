package model;

import exception.*;
import model.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;

    @Id
    private final String accountNumber;
    @MaxLength(30)
    private String ownerName;
    @Positive
    private long balance;                       // no public setter: only deposit()/withdraw() change it
    private boolean active = true;
    private final ArrayList<String> history = new ArrayList<>();

    private static synchronized String nextNumber() {
        return String.format("AC%04d", ++counter);
    }

    /** After loading saved accounts, make sure new numbers don't repeat old ones. */
    public static synchronized void ensureCounterAtLeast(int n) {
        if (n > counter) counter = n;
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = nextNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        if (openingBalance > 0) history.add("OPENED with " + openingBalance);
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    // ----- rules every account type must define -----
    @Override
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    // ----- money movement (thread-safe) -----
    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive, got " + amount);
        balance += amount;
        history.add("DEPOSIT " + amount + " | balance " + balance);
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive, got " + amount);
        if (!active || !canWithdraw(amount)) {
            long shortfall = Math.max(0, amount - balance);
            if (shortfall > 0) throw new InsufficientFundsException(shortfall);
            throw new InsufficientFundsException("Withdrawal not allowed for this account", 0);
        }
        balance -= amount;
        history.add("WITHDRAW " + amount + " | balance " + balance);
        return true;
    }

    /** Locks the account with the smaller number first, so two opposite transfers cannot deadlock. */
    public void transfer(Account to, long amount) throws BankException {
        if (to == this) throw new InvalidAmountException("Cannot transfer to the same account");
        Account first  = accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = (first == this) ? to : this;
        boolean done = false;
        synchronized (first) {
            synchronized (second) {
                try {
                    withdraw(amount);
                    to.deposit(amount);
                    done = true;
                } catch (BankException e) {
                    throw e;                    // rethrow so the caller can show a message
                } finally {
                    history.add("TRANSFER " + amount + " to " + to.accountNumber + (done ? " done" : " FAILED"));
                }
            }
        }
    }

    // ----- getters -----
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName()     { return ownerName; }
    public synchronized long getBalance() { return balance; }
    public boolean isActive()        { return active; }
    public synchronized ArrayList<String> getHistory() { return new ArrayList<>(history); }

    // ----- Object methods (P3) -----
    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + getBalance() + " | " + (active ? "active" : "inactive");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account other)) return false;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }
}
