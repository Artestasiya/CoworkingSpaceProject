package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Exceptions.CoworkingSpaceException;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CoworkingSpaceService {
    private Map<Integer, CoworkingSpace> spaceMap;
    private int nextId = 1;
    private static final String FILE_NAME = "spaces.dat";
    private static final Logger logger = Logger.getLogger(CoworkingSpaceService.class.getName());

    public CoworkingSpaceService() {
        this.spaceMap = new HashMap<>();
        loadFromFile();
    }

    public void addSpace(CoworkingType type, double price, boolean isAvailable) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative. Provided value: " + price);
        }
        int id = nextId++;
        CoworkingSpace space = new CoworkingSpace(id, type, price, isAvailable);
        spaceMap.put(id, space);
        saveToFile();
        logger.info("Added new space: " + space);
    }

    public void removeSpace(int id) throws CoworkingSpaceException {
        if (spaceMap.remove(id) == null) {
            throw new CoworkingSpaceException("Space with ID " + id + " not found.");
        }
        saveToFile();
        logger.info("Removed space with ID: " + id);
    }

    public void toggleAvailability(int id, boolean newAvailability) throws CoworkingSpaceException {
        CoworkingSpace space = spaceMap.get(id);
        if (space != null) {
            space.setAvailable(newAvailability);
            saveToFile();
            logger.info("Toggled availability for space with ID: " + id);
        } else {
            throw new CoworkingSpaceException("Space with ID " + id + " not found.");
        }
    }

    public void displayAvailableSpaces() {
        if (spaceMap.isEmpty()) {
            System.out.println("No spaces available.");
        } else {
            spaceMap.values().stream()
                    .filter(CoworkingSpace::isAvailable)
                    .forEach(space -> {
                        System.out.println("ID: " + space.getId());
                        System.out.println("Type: " + space.getType().getName());
                        System.out.println("Price: " + space.getPrice());
                        System.out.println("------------------------");
                    });
        }
    }

    public Optional<CoworkingSpace> getSpaceById(int id) {
        return Optional.ofNullable(spaceMap.get(id));
    }

    public String displayAllSpaces() {
        if (spaceMap.isEmpty()) {
            return "No spaces available.";
        } else {
            StringBuilder result = new StringBuilder();
            spaceMap.values().forEach(space -> {
                result.append("ID: ").append(space.getId()).append("\n")
                        .append("Type: ").append(space.getType().getName()).append("\n")
                        .append("Price: ").append(space.getPrice()).append("\n")
                        .append("Available: ").append(space.isAvailable()).append("\n")
                        .append("------------------------\n");
            });
            return result.toString();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(spaceMap.values()));
        } catch (IOException e) {
            logger.severe("Error saving data to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<CoworkingSpace> loadedSpaces = (List<CoworkingSpace>) ois.readObject();
            loadedSpaces.forEach(space -> spaceMap.put(space.getId(), space));
            nextId = spaceMap.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error loading data from file: " + e.getMessage());
        }
    }
}