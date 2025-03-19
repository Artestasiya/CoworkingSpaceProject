package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private CoworkingSpaceService spaceService;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        File file = new File("reservations.dat");
        if (file.exists()) {
            file.delete();
        }
        reservationService = new ReservationService(spaceService);
    }

    @Test
    void testAddReservationToNonExistentSpace() {
        when(spaceService.getSpaceById(999)).thenReturn(Optional.empty());
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 999);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testAddReservationToUnavailableSpace() {
        CoworkingSpace space = new CoworkingSpace(1, CoworkingType.OPEN_SPACE, 100.0, false);
        when(spaceService.getSpaceById(1)).thenReturn(Optional.of(space));
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 1);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testCancelReservation() {
        CoworkingSpace space = new CoworkingSpace(1, CoworkingType.OPEN_SPACE, 100.0, true);
        when(spaceService.getSpaceById(1)).thenReturn(Optional.of(space));
        reservationService.addReservation("user1", "2023-10-01", "10:00", "12:00", 1);
        assertEquals(1, reservationService.getReservationsByUser("user1").size());
        reservationService.cancelReservation(1);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }

    @Test
    void testCancelNonExistentReservation() {
        reservationService.cancelReservation(999);
        assertEquals(0, reservationService.getReservationsByUser("user1").size());
    }
}