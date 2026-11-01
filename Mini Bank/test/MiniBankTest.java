import exception.*;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import service.BankService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MiniBankTest {

    @Test
    void depositIncreasesBalance() throws Exception {
        Account a = new SavingsAccount("Test", 1000, 0);
        a.deposit(500);
        assertEquals(1500, a.getBalance());
    }

    @Test
    void withdrawOverBalanceThrowsAndKeepsBalance() {
        Account a = new SavingsAccount("Test", 1000, 0);
        InsufficientFundsException e = assertThrows(InsufficientFundsException.class, () -> a.withdraw(5000));
        assertEquals(4000, e.getShortfall());
        assertEquals(1000, a.getBalance());
    }

    @Test
    void negativeDepositIsRejected() {
        Account a = new CurrentAccount("Test", 100, 0);
        assertThrows(InvalidAmountException.class, () -> a.deposit(-5));
    }

    @Test
    void fixedDepositCannotBeWithdrawn() {
        Account a = new FixedDepositAccount("Test", 10000);
        assertThrows(InsufficientFundsException.class, () -> a.withdraw(100));
    }

    @Test
    void transferMovesMoney() throws Exception {
        Account a = new SavingsAccount("A", 1000, 0);
        Account b = new SavingsAccount("B", 0, 0);
        a.transfer(b, 400);
        assertEquals(600, a.getBalance());
        assertEquals(400, b.getBalance());
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() throws Exception {
        Account acc = new CurrentAccount("Test", 0, 0);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) acc.deposit(1);
                } catch (InvalidAmountException e) {
                    throw new IllegalStateException(e);
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) t.join();
        assertEquals(10000, acc.getBalance());
    }

    @Test
    void invalidMobileIsRejected(@TempDir Path dir) {
        BankService service = new BankService(dir);
        assertThrows(IllegalArgumentException.class,
                () -> service.openAccount("Riya", "riya@example.com", "12345", "SAVINGS", 5000));
    }

    @Test
    void savedStateLoadsUnchanged(@TempDir Path dir) throws Exception {
        BankService first = new BankService(dir);
        Account a = first.openAccount("Riya", "riya@example.com", "9876543210", "SAVINGS", 5000);
        first.deposit(a.getAccountNumber(), 500);
        first.saveState();

        BankService second = new BankService(dir);
        second.loadState();
        assertEquals(5500, second.find(a.getAccountNumber()).getBalance());
    }
}
