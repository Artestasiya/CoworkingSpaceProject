package config;

import Data.DatabaseManager;
import Service.CoworkingSpaceService;
import Service.ReservationService;
import Interface.CoworkingSpaceUI;
import Interface.ReservationUI;
import Interface.MainMenu;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = {"Service", "Interface"})
public class AppConfig {

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("CoworkingPU");
    }

    @Bean
    public DatabaseManager databaseManager(EntityManagerFactory entityManagerFactory) {
        return new DatabaseManager(entityManagerFactory);
    }

    @Bean
    public CoworkingSpaceService coworkingSpaceService(DatabaseManager databaseManager) {
        return new CoworkingSpaceService(databaseManager);
    }

    @Bean
    public ReservationService reservationService(DatabaseManager databaseManager, CoworkingSpaceService coworkingSpaceService) {
        return new ReservationService(databaseManager, coworkingSpaceService);
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public MainMenu mainMenu(CoworkingSpaceUI spaceUI, ReservationUI reservationUI, Scanner scanner) {
        return new MainMenu(spaceUI, reservationUI, scanner);
    }
}
