import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemServiceTest {

    @Test
    void testAddItem() {
        ItemService s = new ItemService();
        s.addItem("Book");
        assertEquals(1, s.getCount());
    }

    @Test
    void testAddingTwoItems() {
        ItemService s = new ItemService();
        s.addItem("Book");
        s.addItem("Pen");
        assertEquals(2, s.getCount());
    }

    @Test
    void testInvalidName() {
        ItemService s = new ItemService();
        assertThrows(IllegalArgumentException.class,
                     () -> s.addItem(""));
    }
}