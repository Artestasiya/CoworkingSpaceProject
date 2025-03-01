package Service;

import Data.CoworkingSpace;
import Data.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private List<Reservation> reservations;
    private CoworkingSpaceService spaceService;

    public ReservationService(CoworkingSpaceService spaceService) {
        this.reservations = new ArrayList<>();
        this.spaceService = spaceService;
        loadReservationsFromFile();
    }

    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId) {
        CoworkingSpace space = spaceService.getSpaceById(spaceId);
        if (space == null || !space.isAvailable()) {
            System.out.println("Space not available or does not exist.");
        } else {
            int id = reservations.size() + 1;
            Reservation reservation = new Reservation(id, userName, date, startTime, endTime, space);
            reservations.add(reservation);
            space.setAvailable(false);
            System.out.println("Reservation successful!");
            saveReservationsToFile();
        }
    }

    public void cancelReservation(int reservationId) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation != null) {
            reservation.getSpace().setAvailable(true);
            reservations.remove(reservation);
            System.out.println("Reservation canceled successfully!");
            saveReservationsToFile();
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public List<Reservation> getReservationsByUser(String userName) {
        return reservations.stream()
                .filter(reservation -> reservation.getUserName().equals(userName))
                .collect(Collectors.toList());
    }

    public void displayAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getId());
                System.out.println("User: " + reservation.getUserName());
                System.out.println("Date: " + reservation.getDate());
                System.out.println("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                System.out.println("Space ID: " + reservation.getSpace().getId());
                System.out.println("------------------------");
            }
        }
    }

    private Reservation getReservationById(int id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void saveReservationsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.dat"))) {
            oos.writeObject(reservations);
            System.out.println("Reservations saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving reservations to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadReservationsFromFile() {
        File file = new File("reservations.dat");
        if (!file.exists()) {
            System.out.println("No reservations file found. Starting with an empty list.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reservations.dat"))) {
            reservations = (List<Reservation>) ois.readObject();
            System.out.println("Reservations loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reservations from file: " + e.getMessage());
        }
    }
}