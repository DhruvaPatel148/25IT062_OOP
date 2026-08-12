public class Driver1 {
    public static void main(String[] args) {
        Card[] table = new Card[5];
        int count = 0;

        Card[] incoming = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Hearts"),
            new Card("Ace", "Spades"),
            new Card("Ten", "Clubs")
        };

        for (Card next : incoming) {
            boolean duplicate = false;

            for (int i = 0; i < count; i++) {
                if (table[i].equals(next)) {
                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {
                System.out.println("Duplicate found: " + next);
            } else {
                table[count++] = next;
                System.out.println("Added: " + next);
            }
        }
    }
}