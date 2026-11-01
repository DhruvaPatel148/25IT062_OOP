import java.util.*;

public class ItemService {
    ArrayList<Item> items = new ArrayList<>();

    public void addItem(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        items.add(new Item(name));
    }

    public int getCount() {
        return items.size();
    }
}