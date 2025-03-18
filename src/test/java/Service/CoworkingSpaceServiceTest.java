package test.java.Service;

import Data.CoworkingType;
import Service.CoworkingSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoworkingSpaceServiceTest {

    private CoworkingSpaceService spaceService;

    @BeforeEach
    void setUp() {
        spaceService = new CoworkingSpaceService();
    }

    @Test
    void testAddSpace_Success() {
        spaceService.addSpace(CoworkingType.OPEN_SPACE, 50.0, true);

        assertEquals(1, spaceService.getAllSpaces().size());
    }
}