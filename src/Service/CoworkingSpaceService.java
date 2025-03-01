package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Exceptions.CoworkingSpaceException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CoworkingSpaceService {
    private List<CoworkingSpace> spaces;
    private int nextId = 1;
    private static final String FILE_NAME = "spaces.dat";

    public CoworkingSpaceService() {
        this.spaces = new ArrayList<>();
        loadFromFile();
    }

    public void addSpace(CoworkingType type, double price, boolean isAvailable) {
        int id = nextId++;
        CoworkingSpace space = new CoworkingSpace(id, type, price, isAvailable);
        spaces.add(space);
        saveToFile();
    }

    public void removeSpace(int id) throws CoworkingSpaceException {
        boolean removed = spaces.removeIf(space -> space.getId() == id);
        if (!removed) {
            throw new CoworkingSpaceException("Space with ID " + id + " not found.");
        }
        saveToFile();
    }

    public void toggleAvailability(int id, boolean newAvailability) throws CoworkingSpaceException {
        CoworkingSpace space = getSpaceById(id);
        if (space != null) {
            space.setAvailable(newAvailability);
            saveToFile();
        } else {
            throw new CoworkingSpaceException("Space with ID " + id + " not found.");
        }
    }

    public void displayAvailableSpaces() {
        if (spaces.isEmpty()) {
            System.out.println("No spaces available.");
        } else {
            for (CoworkingSpace space : spaces) {
                if (space.isAvailable()) {
                    System.out.println("ID: " + space.getId());
                    System.out.println("Type: " + space.getType().getName());
                    System.out.println("Price: " + space.getPrice());
                    System.out.println("------------------------");
                }
            }
        }
    }

    public CoworkingSpace getSpaceById(int id) {
        return spaces.stream()
                .filter(space -> space.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void displayAllSpaces() {
        if (spaces.isEmpty()) {
            System.out.println("No spaces available.");
        } else {
            for (CoworkingSpace space : spaces) {
                System.out.println("ID: " + space.getId());
                System.out.println("Type: " + space.getType().getName());
                System.out.println("Price: " + space.getPrice());
                System.out.println("Available: " + space.isAvailable());
                System.out.println("------------------------");
            }
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(spaces);
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new data file.");
            } catch (IOException e) {
                System.err.println("Error creating data file: " + e.getMessage());
            }
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            spaces = (List<CoworkingSpace>) ois.readObject();
            nextId = spaces.stream().mapToInt(CoworkingSpace::getId).max().orElse(0) + 1;
            System.out.println("Data loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }
}