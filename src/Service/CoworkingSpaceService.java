package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoworkingSpaceService {
    private List<CoworkingSpace> spaces;
    private Scanner scanner;
    private int nextId = 1;

    public CoworkingSpaceService(Scanner scanner) {
        this.spaces = new ArrayList<>();
        this.scanner = scanner;
    }

    public void addSpace() {
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
        boolean isAvailable = scanner.nextBoolean();

        CoworkingSpace space = new CoworkingSpace(id, type, price, isAvailable);
        spaces.add(space);
        System.out.println("Space added successfully! Assigned ID: " + id);
    }

    public void removeSpace() {
        System.out.print("Enter the ID of the space to remove: ");
        int id = scanner.nextInt();
        spaces.removeIf(space -> space.getId() == id);
        if (id > 0){
        System.out.println("Space removed successfully!");}
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
}