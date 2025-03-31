package Interface;

import Data.DatabaseManager;
import Service.CoworkingSpaceService;
import Service.ReservationService;
import java.util.Scanner;

public class MainMenu {
    private final CoworkingSpaceUI spaceUI;
    private final ReservationUI reservationUI;
    private final Scanner scanner;
    private final DatabaseManager dbManager;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.dbManager = new DatabaseManager();
        if (!dbManager.testConnection()) {
            System.err.println("couldn't connect to the database!");
            System.exit(1);
        }

        CoworkingSpaceService spaceService = new CoworkingSpaceService(dbManager);
        ReservationService reservationService = new ReservationService(dbManager, spaceService);

        this.spaceUI = new CoworkingSpaceUI(spaceService, dbManager, scanner);
        this.reservationUI = new ReservationUI(reservationService, scanner);
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
                    case 1:
                        showAdminMenu();
                        break;
                    case 2:
                        showUserMenu();
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice, try again");
                }
            }
        } finally {
            dbManager.closeConnection();
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
                case 1:
                    spaceUI.addSpace();
                    break;
                case 2:
                    spaceUI.removeSpace();
                    break;
                case 3:
                    reservationUI.displayAllReservations();
                    break;
                case 4:
                    spaceUI.displayAllSpaces();
                    break;
                case 5:
                    spaceUI.displayAvailableSpaces();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice, try again");
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
                case 1:
                    spaceUI.displayAvailableSpaces();
                    break;
                case 2:
                    reservationUI.addReservation();
                    break;
                case 3:
                    reservationUI.cancelReservation();
                    break;
                case 4:
                    spaceUI.displayAllSpaces();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, try again");
            }
        }
    }
}