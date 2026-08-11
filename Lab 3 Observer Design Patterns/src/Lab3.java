import java.util.InputMismatchException;
import java.util.Scanner;

public class Lab3 {
    public static void main(String[] args) {
        number n = new number();
        Scanner in = new Scanner(System.in);

        new HexNumber(n);
        new BinNumber(n);

        while (true) {
            try {
                System.out.print("\nEnter a number: ");
                n.setValue(in.nextInt());
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Exiting program.");
                in.next(); // clear the bad token
                break;
            }
        }

        in.close();
    }
}