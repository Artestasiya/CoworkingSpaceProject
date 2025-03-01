package Interface;

import Data.CoworkingSpace;
import Data.Reservation;
import Service.ReservationService;
import java.util.List;
import java.util.Scanner;

public class ReservationUI {
    private ReservationService reservationService;
    private Scanner scanner;

    public ReservationUI(ReservationService reservationService, Scanner scanner) {
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    public void addReservation() {
        System.out.print("Enter your name: ");
        scanner.nextLine();
        String userName = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter start time (HH:MM): ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end time (HH:MM): ");
        String endTime = scanner.nextLine();
        System.out.print("Enter the ID of the space you want to book: ");
        int spaceId = scanner.nextInt();

        reservationService.addReservation(userName, date, startTime, endTime, spaceId);
    }

    public void cancelReservation() {
        System.out.print("Enter your name: ");
        scanner.nextLine();
        String userName = scanner.nextLine();

        List<Reservation> userReservations = reservationService.getReservationsByUser(userName);
        if (userReservations.isEmpty()) {
            System.out.println("No reservations found for " + userName);
        } else {
            for (Reservation reservation : userReservations) {
                System.out.println("Reservation ID: " + reservation.getId());
                System.out.println("Date: " + reservation.getDate());
                System.out.println("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                System.out.println("Space ID: " + reservation.getSpace().getId());
                System.out.println("------------------------");
            }

            System.out.print("Enter the ID of the reservation to cancel (or 0 to go back): ");
            int reservationId = scanner.nextInt();
            if (reservationId != 0) {
                reservationService.cancelReservation(reservationId);
            }
        }
    }

    public void displayAllReservations() {
        reservationService.displayAllReservations();
    }
}