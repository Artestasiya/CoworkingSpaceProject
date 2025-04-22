package com.example.coworking_web.repository;

import com.example.coworking_web.model.CoworkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoworkingSpaceRepository extends JpaRepository<CoworkingSpace, Integer> {
    List<CoworkingSpace> findByAvailableTrue();
}
