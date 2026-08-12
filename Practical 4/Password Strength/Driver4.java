
import java.util.Scanner;

public class Driver4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter password: ");
        String pw = sc.nextLine();

        System.out.println("Length: " + PasswordChecker.hasLength(pw));
        System.out.println("Uppercase: " + PasswordChecker.hasUppercase(pw));
        System.out.println("Digit: " + PasswordChecker.hasDigit(pw));
        System.out.println("Special character: " + PasswordChecker.hasSpecial(pw));
        System.out.println("Strength: " + PasswordChecker.strength(pw));

        sc.close();
    }
}

