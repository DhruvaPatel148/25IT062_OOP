package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {
    /** Reads every *.log file in logsDir, totals deposits/withdrawals, writes and returns the report. */
    public static String generate(Path logsDir, Path reportFile) throws IOException {
        long deposits = 0, withdrawals = 0;
        StringBuilder files = new StringBuilder();

        if (Files.isDirectory(logsDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
                for (Path p : stream) {
                    BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);
                    if (attrs.size() == 0) continue;                       // skip empty files
                    files.append("  ").append(p.getFileName()).append(" | ").append(attrs.size())
                         .append(" bytes | modified ").append(attrs.lastModifiedTime()).append("\n");
                    try (BufferedReader br = Files.newBufferedReader(p)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length < 3) continue;
                            long amount;
                            try { amount = Long.parseLong(parts[2]); } catch (NumberFormatException e) { continue; }
                            if (parts[0].equals("DEPOSIT"))       deposits += amount;
                            else if (parts[0].equals("WITHDRAW")) withdrawals += amount;
                        }
                    }
                }
            }
        }

        String report = "===== END OF DAY REPORT =====\n"
                + "Log files:\n" + (files.length() == 0 ? "  (none)\n" : files)
                + "Total deposits   : " + deposits + "\n"
                + "Total withdrawals: " + withdrawals + "\n"
                + "Net change       : " + (deposits - withdrawals);
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(reportFile, report);
        return report;
    }
}
