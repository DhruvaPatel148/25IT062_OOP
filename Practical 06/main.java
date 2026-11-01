interface Switchable {
    void on();
    void off();
    default void toggle() {
        on();
    }
}

class Fan implements Switchable {
    public void on() { System.out.println("Fan ON"); }
    public void off() { System.out.println("Fan OFF"); }
}

class Light implements Switchable {
    public void on() { System.out.println("Light ON"); }
    public void off() { System.out.println("Light OFF"); }
}

interface Permission {
    boolean a(Switchable device, int hour);
}

public class main {
    public static void main(String[] args) {
        Switchable[] devices = {new Fan(), new Light()};
        for (Switchable d : devices) {
            d.toggle();
        }
        Permission p1 = (device, hour) -> hour > 8 && hour <= 20;
        System.out.println(p1.a(new Fan(), 10));
        System.out.println(p1.a(new Light(), 23));
    }
}
