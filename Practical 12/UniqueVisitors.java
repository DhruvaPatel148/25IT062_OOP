import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        String[] names = {
            "Dhruva", "Priya", "Mauli", "Dhruva",
            "Meera", "Priya", "Aarav"
        };

        HashSet<String> set = new HashSet<>();
        LinkedHashSet<String> orderedSet = new LinkedHashSet<>();

        for (String name : names) {
            set.add(name);
            orderedSet.add(name);
        }

        System.out.println("Unique visitors: " + set.size());

        System.out.println("First three distinct names:");
        int count = 0;

        for (String name : orderedSet) {
            if (count == 3)
                break;

            System.out.println(name);
            count++;
        }
    }
}
