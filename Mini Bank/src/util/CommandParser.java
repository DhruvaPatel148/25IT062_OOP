package util;

public class CommandParser {
    /** Turns "DEPOSIT AC0001 500" into a Command. */
    public static Command parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected: TYPE ACCOUNT AMOUNT (got " + parts.length + " parts)");
        }
        TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
        long amount = Long.parseLong(parts[2]);
        return new Command(type, parts[1].toUpperCase(), amount);
    }
}
