package Interface;

import Data.Reservation;
import Service.ReservationService;
import java.util.List;
import java.util.Scanner;

public class ReservationUI {
    private final ReservationService reservationService;
    private final Scanner scanner;

    public ReservationUI(ReservationService reservationService, Scanner scanner) {
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    public void addReservation() {
        try {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.print("Enter start time (HH:MM): ");
            String startTime = scanner.nextLine();

            System.out.print("Enter end time (HH:MM): ");
            String endTime = scanner.nextLine();

            System.out.print("Enter the ID of the space you want to book: ");
            int spaceId = scanner.nextInt();
            scanner.nextLine();

            reservationService.addReservation(userName, date, startTime, endTime, spaceId);
            System.out.println("Reservation added successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void cancelReservation() {
        try {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            List<Reservation> userReservations = reservationService.getReservationsByUser(userName);
            if (userReservations.isEmpty()) {
                System.out.println("No reservations found for " + userName);
                return;
            }

            System.out.println("\nYour Reservations:");
            for (Reservation reservation : userReservations) {
                System.out.println("ID: " + reservation.getId() +
                        " | Date: " + reservation.getDate() +
                        " | Time: " + reservation.getStartTime() + "-" + reservation.getEndTime() +
                        " | Space: " + reservation.getSpace().getId());
            }

            System.out.print("\nEnter reservation ID to cancel (0 to cancel): ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            if (reservationId != 0) {
                reservationService.cancelReservation(reservationId);
                System.out.println("Reservation cancelled successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void displayAllReservations() {
        try {
            reservationService.displayAllReservations();
        } catch (Exception e) {
            System.err.println("Error displaying reservations: " + e.getMessage());
        }
    }
}