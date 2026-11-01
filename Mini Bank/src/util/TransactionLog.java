package util;

import java.io.IOException;
import java.nio.file.*;

public class TransactionLog {
    /** Append-only log; synchronized so threads don't interleave. */
    public static synchronized void append(Path file, String line) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.write(file, (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
