package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Exceptions.CoworkingSpaceException;
import Service.CoworkingSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoworkingSpaceServiceTest {

    private CoworkingSpaceService service;

    @Test
    void testAddSpaceWithNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addSpace(CoworkingType.OPEN_SPACE, -100.0, true);
        });
    }

    @Test
    void testToggleAvailabilityNonExistentSpace() {
        assertThrows(CoworkingSpaceException.class, () -> {
            service.toggleAvailability(999, false);
        });
    }
}