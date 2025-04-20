package Data;

import jakarta.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DatabaseManager implements AutoCloseable {
    private final EntityManager em;
    private final Map<Integer, String> spaceTypesCache = new ConcurrentHashMap<>();
    private final Map<Integer, CoworkingSpace> spacesCache = new ConcurrentHashMap<>();

    public DatabaseManager(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
        testConnection();
    }

    private void testConnection() {
        try {
            boolean connected = em.createQuery("SELECT 1", Integer.class).getSingleResult() == 1;
            System.out.println("Database connection test: " + (connected ? "SUCCESS" : "FAILED"));
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new PersistenceException("Failed to connect to database", e);
        }
    }

    public Optional<CoworkingType> getCoworkingTypeById(int typeId) {
        try {
            return Optional.ofNullable(em.find(CoworkingType.class, typeId));
        } catch (Exception e) {
            throw new PersistenceException("Error getting coworking type by ID", e);
        }
    }

    public boolean isSpaceAvailable(int spaceId, String date, String startTime, String endTime) {
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Reservation r WHERE r.space.id = :spaceId AND r.date = :date AND " +
                                    "((r.startTime < :endTime AND r.endTime > :startTime))", Long.class)
                    .setParameter("spaceId", spaceId)
                    .setParameter("date", date)
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime)
                    .getSingleResult();
            return count == 0;
        } catch (Exception e) {
            throw new PersistenceException("Error checking space availability", e);
        }
    }

    public void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    public void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void addReservation(Reservation reservation) {
        executeInTransaction(() -> em.persist(reservation));
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        try {
            return Optional.ofNullable(em.find(Reservation.class, reservationId));
        } catch (Exception e) {
            throw new PersistenceException("Error getting reservation by ID", e);
        }
    }

    public void cancelReservation(int reservationId) {
        executeInTransaction(() -> {
            Reservation reservation = em.find(Reservation.class, reservationId);
            if (reservation != null) {
                em.remove(reservation);
            }
        });
    }

    public void refreshSpaceTypesCache() {
        try {
            List<CoworkingType> types = em.createQuery("SELECT t FROM CoworkingType t", CoworkingType.class)
                    .getResultList();
            spaceTypesCache.clear();
            spaceTypesCache.putAll(types.stream()
                    .collect(Collectors.toMap(CoworkingType::getId, CoworkingType::getName)));
        } catch (Exception e) {
            throw new PersistenceException("Error refreshing space types cache", e);
        }
    }

    public Map<Integer, String> getAllSpaceTypes() {
        if (spaceTypesCache.isEmpty()) {
            refreshSpaceTypesCache();
        }
        return new HashMap<>(spaceTypesCache);
    }

    public String getTypeNameById(int typeId) {
        return spaceTypesCache.getOrDefault(typeId, "Unknown Type");
    }

    public void addCoworkingSpace(CoworkingSpace space) {
        executeInTransaction(() -> {
            em.persist(space);
            spacesCache.put(space.getId(), space);
        });
    }

    public Optional<CoworkingSpace> getCoworkingSpaceById(int id) {
        try {
            return Optional.ofNullable(spacesCache.computeIfAbsent(id,
                    k -> em.find(CoworkingSpace.class, id)));
        } catch (Exception e) {
            throw new PersistenceException("Error getting coworking space by ID", e);
        }
    }

    public List<CoworkingSpace> getAllCoworkingSpaces() {
        try {
            return em.createQuery("SELECT s FROM CoworkingSpace s", CoworkingSpace.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error getting all coworking spaces", e);
        }
    }

    public void updateCoworkingSpaceAvailability(int id, boolean isAvailable) {
        executeInTransaction(() -> {
            CoworkingSpace space = em.find(CoworkingSpace.class, id);
            if (space != null) {
                space.setAvailable(isAvailable);
                spacesCache.put(id, space);
            }
        });
    }

    public void deleteCoworkingSpace(int id) {
        executeInTransaction(() -> {
            CoworkingSpace space = em.find(CoworkingSpace.class, id);
            if (space != null) {
                em.remove(space);
                spacesCache.remove(id);
            }
        });
    }

    public List<Reservation> getReservationsForSpace(int spaceId) {
        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.space.id = :spaceId", Reservation.class)
                    .setParameter("spaceId", spaceId)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error getting reservations for space", e);
        }
    }

    public List<Reservation> getReservationsByUser(String userName) {
        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.userName = :userName", Reservation.class)
                    .setParameter("userName", userName)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error getting reservations by user", e);
        }
    }

    public List<Reservation> getAllReservations() {
        try {
            return em.createQuery("SELECT r FROM Reservation r", Reservation.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error getting all reservations", e);
        }
    }

    private void executeInTransaction(Runnable operation) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Transaction failed", e);
        }
    }

    @Override
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}