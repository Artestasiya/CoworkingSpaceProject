package com.example.coworking_web.controller;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.service.CoworkingSpaceService;
import com.example.coworking_web.exceptions.CoworkingSpaceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spaces")
public class CoworkingSpaceController {
    private final CoworkingSpaceService spaceService;

    public CoworkingSpaceController(CoworkingSpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @PostMapping
    public ResponseEntity<?> addSpace(@RequestBody SpaceDTO dto) {
        try {
            spaceService.addSpace(dto.getTypeId(), dto.getPrice(), dto.isAvailable());
            return ResponseEntity.ok("Space added successfully");
        } catch (CoworkingSpaceException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/available")
    public List<CoworkingSpace> getAvailableSpaces() {
        return spaceService.getAvailableSpaces();
    }

    @GetMapping
    public List<CoworkingSpace> getAllSpaces() {
        return spaceService.getAllSpaces();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeSpace(@PathVariable int id) {
        try {
            spaceService.removeSpace(id);
            return ResponseEntity.ok("Space removed successfully");
        } catch (CoworkingSpaceException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}