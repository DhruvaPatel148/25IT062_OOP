import java.util.Objects;

public class Card {
    private String rank, suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Card))
            return false;

        Card c = (Card) o;
        return rank.equals(c.rank) && suit.equals(c.suit);
    }

    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}