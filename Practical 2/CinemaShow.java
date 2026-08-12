public class CinemaShow {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    public CinemaShow(String title) {
        this(title, 100);
    }

    public boolean book(int n) {
        if (n > 0 && n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        }
        return false;
    }

    public void cancel(int n) {
        if (n > 0) {
            int booked = capacity - seatsAvailable;

            if (n > booked)
                n = booked;

            seatsAvailable += n;
            totalBooked -= n;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {
        CinemaShow a = new CinemaShow("Interstellar", 5);
        CinemaShow b = new CinemaShow("Dune");

        System.out.println("A book 3 -> " + a.book(3) + " | seats " + a.getSeatsAvailable());
        System.out.println("A book 4 -> " + a.book(4) + " | seats " + a.getSeatsAvailable());

        a.cancel(1);
        System.out.println("A cancel 1 | seats " + a.getSeatsAvailable());

        System.out.println("B book 10 -> " + b.book(10) + " | seats " + b.getSeatsAvailable());

        System.out.println("Total booked across shows: " + CinemaShow.getTotalBooked());
    }
}