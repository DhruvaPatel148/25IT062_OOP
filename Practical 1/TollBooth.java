import java.util.Scanner;

class Vehicle {
    String number;
    String type;

    Vehicle(String number, String type) {
        this.number = number;
        this.type = type;
    }
}

public class TollBooth {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int total = 0;
        int bike = 0, car = 0, truck = 0;

        while (true) {
            String number = sc.next();

            if (number.equalsIgnoreCase("done"))
                break;

            String type = sc.next().toLowerCase();

            Vehicle v = new Vehicle(number, type);

            int toll = 0;

            switch (v.type) {
                case "bike":
                    toll = 20;
                    bike++;
                    break;
                case "car":
                    toll = 50;
                    car++;
                    break;
                case "truck":
                    toll = 150;
                    truck++;
                    break;
            }

            total += toll;
        }

        System.out.println("Total toll: " + total);

        if (bike >= car && bike >= truck)
            System.out.println("Most frequent: bike");
        else if (car >= bike && car >= truck)
            System.out.println("Most frequent: car");
        else
            System.out.println("Most frequent: truck");
    }
}