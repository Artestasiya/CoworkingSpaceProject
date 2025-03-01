package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Exceptions.CoworkingSpaceException;
import Exceptions.InvalidAvailabilityInputException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoworkingSpaceService {
    private List<CoworkingSpace> spaces;
    private Scanner scanner;
    private int nextId = 1;
    private static final String FILE_NAME = "spaces.dat";

    public CoworkingSpaceService(Scanner scanner) {
        this.scanner = scanner;
        this.spaces = new ArrayList<>();
        loadFromFile();
    }

    public void addSpace() {
        try {
            System.out.println("Enter space details:");

            int id = nextId++;

            scanner.nextLine();

            System.out.println("Available types:");
            for (CoworkingType type : CoworkingType.values()) {
                System.out.println(type.getId() + ": " + type.getName());
            }
            System.out.print("Enter type ID: ");
            int typeId = scanner.nextInt();
            CoworkingType type = CoworkingType.getById(typeId);

            System.out.print("Price: ");
            double price = scanner.nextDouble();

            System.out.print("Is available (true/false): ");
            String availabilityInput = scanner.next().toLowerCase();
            boolean isAvailable;

            if (availabilityInput.equals("true")) {
                isAvailable = true;
            } else if (availabilityInput.equals("false")) {
                isAvailable = false;
            } else {
                throw new InvalidAvailabilityInputException("Invalid input for availability. Please enter 'true' or 'false'.");
            }

            CoworkingSpace space = new CoworkingSpace(id, type, price, isAvailable);
            spaces.add(space);
            System.out.println("Space added successfully! Assigned ID: " + id);

            saveToFile();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InvalidAvailabilityInputException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    public void removeSpace() {
        try {
            System.out.print("Enter the ID of the space to remove: ");
            int id = scanner.nextInt();
            boolean removed = spaces.removeIf(space -> space.getId() == id);
            if (removed) {
                System.out.println("Space removed successfully!");
                saveToFile();
            } else {
                throw new CoworkingSpaceException("Space with ID " + id + " not found.");
            }
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    public void toggleAvailability() {
        try {
            System.out.print("Enter the ID of the space to toggle availability: ");
            int id = scanner.nextInt();
            CoworkingSpace space = getSpaceById(id);
            if (space != null) {
                System.out.print("Enter new availability (true/false): ");
                String availabilityInput = scanner.next().toLowerCase();
                boolean newAvailability;

                if (availabilityInput.equals("true")) {
                    newAvailability = true;
                } else if (availabilityInput.equals("false")) {
                    newAvailability = false;
                } else {
                    throw new InvalidAvailabilityInputException("Invalid input for availability. Please enter 'true' or 'false'.");
                }

                space.setAvailable(newAvailability);
                System.out.println("Availability updated successfully!");
                saveToFile();
            } else {
                throw new CoworkingSpaceException("Space with ID " + id + " not found.");
            }
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InvalidAvailabilityInputException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
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

    private void saveToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(spaces);
            System.out.println("Data saved to file.");
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