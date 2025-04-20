package Interface;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Exceptions.CoworkingSpaceException;
import Exceptions.InvalidAvailabilityInputException;
import Service.CoworkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class CoworkingSpaceUI {
    private final CoworkingSpaceService spaceService;
    private final DatabaseManager dbManager;
    private final Scanner scanner;

    @Autowired
    public CoworkingSpaceUI(CoworkingSpaceService spaceService,
                            DatabaseManager dbManager,
                            Scanner scanner) {
        this.spaceService = spaceService;
        this.dbManager = dbManager;
        this.scanner = scanner;
    }

    public void addSpace() {
        try {
            System.out.println("\n=== Add New Coworking Space ===");
            System.out.print("Enter type ID: ");
            int typeId = scanner.nextInt();

            System.out.print("Enter price: ");
            double price = scanner.nextDouble();

            System.out.print("Is available (true/false): ");
            boolean isAvailable = scanner.nextBoolean();

            spaceService.addSpace(typeId, price, isAvailable);
            System.out.println("Space added successfully!");

        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
        } finally {
            scanner.nextLine();
        }
    }

    public void displayAvailableSpaces() {
        try {
            List<CoworkingSpace> spaces = spaceService.getAvailableSpaces();

            System.out.println("\n=== Available Spaces ===");
            System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Type", "Price", "Status");
            System.out.println("--------------------------------------------");

            for (CoworkingSpace space : spaces) {
                String typeName = space.getType() != null ? space.getType().getName() : "Unknown";
                System.out.printf("%-5d %-20s %-10.2f %-10s%n",
                        space.getId(),
                        typeName,
                        space.getPrice(),
                        space.isAvailable() ? "Available" : "Booked");
            }
        } catch (Exception e) {
            System.err.println("Error displaying spaces: " + e.getMessage());
        }
    }

    public void displayAllSpaces() {
        try {
            List<CoworkingSpace> spaces = spaceService.getAllSpaces();

            System.out.println("\n=== All Coworking Spaces ===");
            System.out.printf("%-5s %-20s %-10s %-10s %-15s%n",
                    "ID", "Type", "Price", "Status", "Reservations");
            System.out.println("-------------------------------------------------------");

            for (CoworkingSpace space : spaces) {
                String typeName = space.getType() != null ? space.getType().getName() : "Unknown";
                System.out.printf("%-5d %-20s %-10.2f %-10s %-15d%n",
                        space.getId(),
                        typeName,
                        space.getPrice(),
                        space.isAvailable() ? "Available" : "Booked",
                        space.getReservations().size());
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

            System.out.print("Are you sure? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                spaceService.removeSpace(id);
                System.out.println("Space removed successfully!");
            } else {
                System.out.println("Operation canceled.");
            }
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
        }
    }
}