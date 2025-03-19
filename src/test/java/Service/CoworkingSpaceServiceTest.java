package Service;

import Data.CoworkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CoworkingSpaceServiceTest {

    private CoworkingSpaceService spaceService;

    @BeforeEach
    void setUp() {
        spaceService = new CoworkingSpaceService();
        spaceService.clearSpaces();

        File file = new File("spaces.dat");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testDisplayAllSpaces_NoSpaces() {
        String result = spaceService.displayAllSpaces();
        assertEquals("No spaces available.", result);
    }

    @Test
    void testDisplayAllSpaces_WithSpaces() {

        spaceService.addSpace(CoworkingType.OPEN_SPACE, 50.0, true);

        String expectedOutput = "ID: 1\n" +
                "Type: Open Space\n" +
                "Price: 50.0\n" +
                "Available: true\n" +
                "------------------------\n";

        String result = spaceService.displayAllSpaces();
        assertEquals(expectedOutput, result);
    }

    @Test
    void testDisplayAllSpaces_MultipleSpaces() {
        spaceService.addSpace(CoworkingType.OPEN_SPACE, 50.0, true);
        spaceService.addSpace(CoworkingType.MEETING_ROOM, 100.0, false);

        String expectedOutput = "ID: 2\n" +
                "Type: Open Space\n" +
                "Price: 50.0\n" +
                "Available: true\n" +
                "------------------------\n" +
                "ID: 3\n" +
                "Type: Meeting Room\n" +
                "Price: 100.0\n" +
                "Available: false\n" +
                "------------------------\n";

        String result = spaceService.displayAllSpaces();
        assertEquals(expectedOutput, result);
    }
}