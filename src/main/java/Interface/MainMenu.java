package Interface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class MainMenu {
    private final CoworkingSpaceUI spaceUI;
    private final ReservationUI reservationUI;
    private final Scanner scanner;

    @Autowired
    public MainMenu(CoworkingSpaceUI spaceUI, ReservationUI reservationUI, Scanner scanner) {
        this.spaceUI = spaceUI;
        this.reservationUI = reservationUI;
        this.scanner = scanner;
    }

    public void showMainMenu() {
        try {
            while (true) {
                System.out.println("\nWelcome to Cowork");
                System.out.println("1. Admin");
                System.out.println("2. User");
                System.out.println("3. Exit");
                System.out.print("Your role? ");

                int role = scanner.nextInt();
                scanner.nextLine();

                switch (role) {
                    case 1 -> showAdminMenu();
                    case 2 -> showUserMenu();
                    case 3 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice, try again");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add new coworking space");
            System.out.println("2. Remove coworking space");
            System.out.println("3. View all reservations");
            System.out.println("4. View all spaces");
            System.out.println("5. Browse available spaces");
            System.out.println("6. Back to main menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> spaceUI.addSpace();
                case 2 -> spaceUI.removeSpace();
                case 3 -> reservationUI.displayAllReservations();
                case 4 -> spaceUI.displayAllSpaces();
                case 5 -> spaceUI.displayAvailableSpaces();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice, try again");
            }
        }
    }

    private void showUserMenu() {
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. Browse available spaces");
            System.out.println("2. Make a reservation");
            System.out.println("3. View and cancel reservations");
            System.out.println("4. View all spaces");
            System.out.println("5. Back to main menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> spaceUI.displayAvailableSpaces();
                case 2 -> reservationUI.addReservation();
                case 3 -> reservationUI.cancelReservation();
                case 4 -> spaceUI.displayAllSpaces();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice, try again");
            }
        }
    }
}