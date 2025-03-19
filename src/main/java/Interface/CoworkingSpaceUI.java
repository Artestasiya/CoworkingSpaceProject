package Interface;

import Data.CoworkingType;
import Exceptions.CoworkingSpaceException;
import Exceptions.InvalidAvailabilityInputException;
import Service.CoworkingSpaceService;

import java.util.Scanner;

public class CoworkingSpaceUI {
    private CoworkingSpaceService spaceService;
    private Scanner scanner;

    public CoworkingSpaceUI(CoworkingSpaceService spaceService, Scanner scanner) {
        this.spaceService = spaceService;
        this.scanner = scanner;
    }

    public void addSpace() {
        try {
            System.out.println("Enter space details:");

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

            spaceService.addSpace(type, price, isAvailable);
            System.out.println("Space added successfully!");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InvalidAvailabilityInputException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void removeSpace() {
        try {
            System.out.print("Enter the ID of the space to remove: ");
            int id = scanner.nextInt();
            spaceService.removeSpace(id);
            System.out.println("Space removed successfully!");
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void toggleAvailability() {
        try {
            System.out.print("Enter the ID of the space to toggle availability: ");
            int id = scanner.nextInt();
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

            spaceService.toggleAvailability(id, newAvailability);
            System.out.println("Availability updated successfully!");
        } catch (CoworkingSpaceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InvalidAvailabilityInputException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void displayAvailableSpaces() {
        spaceService.displayAvailableSpaces();
    }

    public void displayAllSpaces() {
        spaceService.displayAllSpaces();
    }
}