package Interface;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Exceptions.CoworkingSpaceException;
import Exceptions.InvalidAvailabilityInputException;
import Service.CoworkingSpaceService;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CoworkingSpaceUI {
    private final CoworkingSpaceService spaceService;
    private final DatabaseManager dbManager;
    private final Scanner scanner;

    public CoworkingSpaceUI(CoworkingSpaceService spaceService, DatabaseManager dbManager, Scanner scanner) {
        this.spaceService = spaceService;
        this.dbManager = dbManager;
        this.scanner = scanner;
    }

    public void addSpace() {
        try {
            System.out.println("Enter space details:");
            scanner.nextLine();

            Map<Integer, String> spaceTypes = dbManager.getAllSpaceTypes();
            System.out.println("Available types:");
            spaceTypes.forEach((id, name) -> System.out.println(id + ": " + name));

            System.out.print("Enter type ID: ");
            int typeId = scanner.nextInt();

            if (!spaceTypes.containsKey(typeId)) {
                System.err.println("Error: Invalid type ID");
                return;
            }

            System.out.print("Price: ");
            double price = scanner.nextDouble();

            System.out.print("Is available (true/false): ");
            String availabilityInput = scanner.next().toLowerCase();
            boolean isAvailable = availabilityInput.equals("true");

            if (!availabilityInput.equals("true") && !availabilityInput.equals("false")) {
                throw new InvalidAvailabilityInputException("Invalid input for availability. Please enter 'true' or 'false'.");
            }

            spaceService.addSpace(typeId, price, isAvailable);
            System.out.println("Space added successfully!");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (CoworkingSpaceException e) {
            System.err.println("Error adding space: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void displayAvailableSpaces() {
        try {
            Map<Integer, String> spaceTypes = dbManager.getAllSpaceTypes();
            List<CoworkingSpace> spaces = spaceService.getAvailableSpaces();

            System.out.println("\nAvailable Spaces:");
            System.out.println("ID\tType\t\tPrice");
            System.out.println("------------------------------");

            for (CoworkingSpace space : spaces) {
                String typeName = spaceTypes.getOrDefault(space.getTypeId(), "Unknown");
                System.out.printf("%d\t%-15s\t%.2f\n",
                        space.getId(),
                        typeName,
                        space.getPrice());
            }
        } catch (Exception e) {
            System.err.println("Error displaying available spaces: " + e.getMessage());
        }
    }

    public void displayAllSpaces() {
        try {
            Map<Integer, String> spaceTypes = dbManager.getAllSpaceTypes();
            List<CoworkingSpace> spaces = spaceService.getAllSpaces();

            System.out.println("\nAll Coworking Spaces:");
            System.out.println("ID\tType\t\tPrice\tAvailable");
            System.out.println("----------------------------------");

            for (CoworkingSpace space : spaces) {
                String typeName = spaceTypes.getOrDefault(space.getTypeId(), "Unknown");
                System.out.printf("%d\t%-15s\t%.2f\t%s\n",
                        space.getId(),
                        typeName,
                        space.getPrice(),
                        space.isAvailable() ? "Yes" : "No");
            }
        } catch (Exception e) {
            System.err.println("Error displaying spaces: " + e.getMessage());
        }
    }

    public void removeSpace() {
        try {
            System.out.print("\nEnter space ID to remove: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Are you sure you want to remove space " + id + "? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                spaceService.removeSpace(id);
                System.out.println("Space successfully removed!");
            }
        } catch (InputMismatchException e) {
            System.err.println("Error: Please enter a valid number for space ID");
            scanner.nextLine();
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}