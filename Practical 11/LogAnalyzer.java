import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LogAnalyzer {
    public static void main(String[] args) throws Exception {
        String[] files = {"log1.txt", "log2.txt"};
        String keyword = "error";
        int totalLines = 0, count = 0;

        Files.write(Paths.get("log1.txt"),
            Arrays.asList("Application started", "error found"));

        Files.write(Paths.get("log2.txt"),
            Arrays.asList("System running", "error occurred", "Task completed"));

        for (String file : files) {
            Path path = Paths.get(file);
            long size = Files.size(path);

            System.out.println(file + " size: " + size + " bytes");

            BufferedReader br = Files.newBufferedReader(path);
            String line;

            while ((line = br.readLine()) != null) {
                totalLines++;

                if (line.toLowerCase().contains(keyword))
                    count++;
            }

            br.close();
        }

        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing keyword: " + count);
    }
}