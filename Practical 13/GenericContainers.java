class Box<T> {
    T value;

    void set(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}

class Pair<A, B> {
    A first;
    B second;

    Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    void display() {
        System.out.println(first + " " + second);
    }
}

public class GenericContainers {
    static <T> T first(T[] arr) {
        return arr[0];
    }

    public static void main(String[] args) {
        Box<Integer> b1 = new Box<>();
        b1.set(10);
        System.out.println(b1.get());

        Box<String> b2 = new Box<>();
        b2.set("Java");
        System.out.println(b2.get());

        Pair<Integer, String> p = new Pair<>(1, "Dhruva");
        p.display();

        Integer[] arr = {5, 10, 15};
        System.out.println(first(arr));
    }
}