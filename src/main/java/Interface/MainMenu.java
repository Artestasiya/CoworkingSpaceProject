package Interface;

import Service.CoworkingSpaceService;
import Service.ReservationService;
import java.util.Scanner;

public class MainMenu {
    private Interface.CoworkingSpaceUI spaceUI;
    private Interface.ReservationUI reservationUI;
    private Scanner scanner;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        CoworkingSpaceService spaceService = new CoworkingSpaceService();
        ReservationService reservationService = new ReservationService(spaceService);
        this.spaceUI = new Interface.CoworkingSpaceUI(spaceService, scanner);
        this.reservationUI = new Interface.ReservationUI(reservationService, scanner);
    }

    public void showMainMenu() {
        System.out.println("Welcome to Cowork");
        System.out.println("Send 1 to Admin");
        System.out.println("Send 2 to User");
        System.out.println("Yours role ? ");
        int role = scanner.nextInt();
        switch (role) {
            case 1:
                showAdminMenu();
                break;
            case 2:
                showUserMenu();
                break;
            default:
                System.out.println("Undefined role, try again");
                showMainMenu();
        }
    }

    private void showAdminMenu() {
        System.out.println("Admin Menu");
        System.out.println("1. Add new coworking space");
        System.out.println("2. Remove coworking space");
        System.out.println("3. View all reservations");
        System.out.println("4. Types of spaces");
        System.out.println("5. Browse available spaces");
        System.out.println("6. Back to main menu");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                spaceUI.addSpace();
                showAdminMenu();
                break;
            case 2:
                spaceUI.removeSpace();
                showAdminMenu();
                break;
            case 3:
                reservationUI.displayAllReservations();
                showAdminMenu();
                break;
            case 4:
                spaceUI.displayAllSpaces();
                showAdminMenu();
                break;
            case 5:
                spaceUI.displayAvailableSpaces();
                showAdminMenu();
                break;
            case 6:
                showMainMenu();
                break;
            default:
                System.out.println("Invalid choice, try again");
                showAdminMenu();
        }
    }

    private void showUserMenu() {
        System.out.println("User Menu");
        System.out.println("1. Browse available spaces");
        System.out.println("2. Make a reservation");
        System.out.println("3. View and cancel reservations");
        System.out.println("4. Types of spaces");
        System.out.println("5. Back to main menu");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                spaceUI.displayAvailableSpaces();
                showUserMenu();
                break;
            case 2:
                reservationUI.addReservation();
                showUserMenu();
                break;
            case 3:
                reservationUI.cancelReservation();
                showUserMenu();
                break;
            case 4:
                spaceUI.displayAllSpaces();
                showUserMenu();
                break;
            case 5:
                showMainMenu();
                break;
            default:
                System.out.println("Invalid choice, try again");
                showUserMenu();
        }
    }
}