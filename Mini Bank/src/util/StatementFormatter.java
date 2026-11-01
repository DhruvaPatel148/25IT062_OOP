package util;

import model.Account;

public class StatementFormatter {
    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== STATEMENT ==========\n");
        sb.append("Account : ").append(account.getAccountNumber()).append("\n");
        sb.append("Owner   : ").append(account.getOwnerName()).append("\n");
        sb.append("Type    : ").append(account.getClass().getSimpleName()).append("\n");
        sb.append("---------- Transactions -------\n");
        for (String line : account.getHistory()) {
            sb.append("  ").append(line).append("\n");
        }
        sb.append("-------------------------------\n");
        sb.append("Balance : ").append(account.getBalance()).append("\n");
        sb.append("===============================");
        return sb.toString();
    }
}
