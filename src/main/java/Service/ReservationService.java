package Service;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.ReservationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final DatabaseManager dbManager;
    private final CoworkingSpaceService spaceService;

    public ReservationService(DatabaseManager dbManager, CoworkingSpaceService spaceService) {
        this.dbManager = dbManager;
        this.spaceService = spaceService;
    }

    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId)
            throws ReservationException {
        try {
            dbManager.beginTransaction();

            Optional<CoworkingSpace> spaceOpt = spaceService.getSpaceById(spaceId);
            if (spaceOpt.isEmpty() || !spaceOpt.get().isAvailable()) {
                throw new ReservationException("Пространство недоступно или не существует");
            }

            if (!dbManager.isSpaceAvailable(spaceId, date, startTime, endTime)) {
                throw new ReservationException("Пространство уже занято в указанное время");
            }

            Reservation reservation = new Reservation(0, userName, date, startTime, endTime, spaceOpt.get());
            dbManager.addReservation(reservation);
            dbManager.updateCoworkingSpaceAvailability(spaceId, false);

            dbManager.commitTransaction();
        } catch (Exception e) {
            dbManager.rollbackTransaction();
            throw new ReservationException("Не удалось создать бронирование: " + e.getMessage(), e);
        }
    }

    public void cancelReservation(int reservationId) throws ReservationException {
        try {
            dbManager.beginTransaction();

            Optional<Reservation> reservationOpt = dbManager.getReservationById(reservationId);
            if (reservationOpt.isEmpty()) {
                throw new ReservationException("Бронирование с ID " + reservationId + " не найдено");
            }

            Reservation reservation = reservationOpt.get();
            dbManager.cancelReservation(reservationId);

            // Обновляем доступность пространства
            dbManager.updateCoworkingSpaceAvailability(reservation.getSpace().getId(), true);

            dbManager.commitTransaction();
        } catch (SQLException e) {
            dbManager.rollbackTransaction();
            throw new ReservationException("Не удалось отменить бронирование: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getReservationsByUser(String userName) throws ReservationException {
        try {
            return dbManager.getReservationsByUser(userName);
        } catch (SQLException e) {
            throw new ReservationException("Ошибка при получении бронирований пользователя", e);
        }
    }

    public void displayAllReservations() {
        try {
            List<Reservation> reservations = dbManager.getAllReservations();
            if (reservations.isEmpty()) {
                System.out.println("Нет активных бронирований");
                return;
            }

            System.out.println("\nВсе бронирования:");
            System.out.println("-----------------------");
            reservations.forEach(reservation -> {
                System.out.println("ID: " + reservation.getId());
                System.out.println("Пользователь: " + reservation.getUserName());
                System.out.println("Дата: " + reservation.getDate());
                System.out.println("Время: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                System.out.println("Пространство ID: " + reservation.getSpace().getId());
                try {
                    System.out.println("Тип: " + reservation.getSpace().getType().getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("-----------------------");
            });
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка бронирований: " + e.getMessage());
        }
    }
}