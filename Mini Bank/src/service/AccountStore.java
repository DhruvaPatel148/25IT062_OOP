package service;

import model.Account;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/** Practical 12: accounts in a HashMap, history in an ArrayList. */
public class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> history = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        history.add("ADDED " + account.getAccountNumber());
    }

    public Account get(String accountNumber)        { return accounts.get(accountNumber); }
    public ArrayList<Account> getAll()              { return new ArrayList<>(accounts.values()); }
    public ArrayList<String> getHistory()           { return history; }

    /** Highest balance first. */
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    public ArrayList<Account> balanceAbove(long value) {
        ArrayList<Account> result = new ArrayList<>();
        for (Account a : accounts.values()) if (a.getBalance() > value) result.add(a);
        return result;
    }
}
