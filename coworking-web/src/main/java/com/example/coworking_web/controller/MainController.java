package com.example.coworking_web.controller;

import com.example.coworking_web.service.CoworkingSpaceService;
import com.example.coworking_web.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final CoworkingSpaceService coworkingSpaceService;
    private final ReservationService reservationService;

    @Autowired
    public MainController(CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String showMainMenu() {
        return """
                Welcome to Cowork
                1. Admin
                2. User
                3. Exit
                Please choose a role:
                """;
    }

    @GetMapping("/admin")
    public String showAdminMenu() {
        return """
                Admin Menu:
                1. Add new coworking space
                2. Remove coworking space
                3. View all reservations
                4. View all spaces
                5. Browse available spaces
                6. Back to main menu
                """;
    }

    @GetMapping("/user")
    public String showUserMenu() {
        return """
                User Menu:
                1. Browse available spaces
                2. Make a reservation
                3. View and cancel reservations
                4. View all spaces
                5. Back to main menu
                """;
    }

    // Дополнительные маршруты для выполнения действий администратора или пользователя
    @GetMapping("/admin/addSpace")
    public String addSpace(@RequestParam("typeId") int typeId, @RequestParam("price") double price, @RequestParam("isAvailable") boolean isAvailable) {
        try {
            coworkingSpaceService.addSpace(typeId, price, isAvailable);
            return "Space added successfully!";
        } catch (Exception e) {
            return "Failed to add space: " + e.getMessage();
        }
    }

    @GetMapping("/user/reserveSpace")
    public String makeReservation(@RequestParam("userName") String userName, @RequestParam("date") String date, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("spaceId") int spaceId) {
        try {
            reservationService.addReservation(userName, date, startTime, endTime, spaceId);
            return "Reservation made successfully!";
        } catch (Exception e) {
            return "Failed to make reservation: " + e.getMessage();
        }
    }
}
