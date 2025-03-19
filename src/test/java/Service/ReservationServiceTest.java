package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Data.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private CoworkingSpaceService spaceService;

    @BeforeEach
    void setUp() {
        spaceService = new CoworkingSpaceService();
        reservationService = new ReservationService(spaceService);
    }

    @Test
    void testAddReservationToNonExistentSpace() {
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 999);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testAddReservationToUnavailableSpace() {
        spaceService.addSpace(CoworkingType.OPEN_SPACE, 100.0, false);
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 1);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testCancelReservation() {
        spaceService.addSpace(CoworkingType.OPEN_SPACE, 100.0, true);
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 1);
        reservationService.cancelReservation(1);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testCancelNonExistentReservation() {
        reservationService.cancelReservation(999);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }
}