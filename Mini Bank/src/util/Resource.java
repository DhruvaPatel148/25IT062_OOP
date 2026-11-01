package util;

/** Tiny AutoCloseable used to show try-with-resources (P8). */
public class Resource implements AutoCloseable {
    private final String name;

    public Resource(String name) {
        this.name = name;
        System.out.println("  [" + name + "] opened");
    }

    public void use(boolean fail) {
        System.out.println("  [" + name + "] working");
        if (fail) throw new IllegalStateException("something went wrong inside the block");
    }

    @Override
    public void close() {
        System.out.println("  [" + name + "] closed");
    }
}
