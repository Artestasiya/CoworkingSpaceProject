package Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=CoworkingDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "Admin";
    private static final String PASS = "Admin";

    private static Connection connection;
    private Map<Integer, String> spaceTypesCache;

    public boolean isSpaceAvailable(int spaceId, String date, String startTime, String endTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE space_id = ? AND date = ? AND " +
                "((start_time < ? AND end_time > ?) OR " +
                "(start_time < ? AND end_time > ?) OR " +
                "(start_time >= ? AND end_time <= ?))";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, spaceId);
            pstmt.setString(2, date);
            pstmt.setString(3, endTime);
            pstmt.setString(4, startTime);
            pstmt.setString(5, endTime);
            pstmt.setString(6, startTime);
            pstmt.setString(7, startTime);
            pstmt.setString(8, endTime);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (user_name, date, start_time, end_time, space_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, reservation.getUserName());
            pstmt.setString(2, reservation.getDate());
            pstmt.setString(3, reservation.getStartTime());
            pstmt.setString(4, reservation.getEndTime());
            pstmt.setInt(5, reservation.getSpace().getId());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Reservation> getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT r.id, r.user_name, r.date, r.start_time, r.end_time, " +
                "cs.id as space_id, cs.type_id, cs.price, cs.is_available " +
                "FROM Reservation r JOIN CoworkingSpace cs ON r.space_id = cs.id " +
                "WHERE r.id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapReservation(rs));
                }
            }
        }
        return Optional.empty();
    }

    public void cancelReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        CoworkingSpace space = new CoworkingSpace(
                rs.getInt("space_id"),
                rs.getInt("type_id"),
                rs.getDouble("price"),
                rs.getBoolean("is_available")
        );

        return new Reservation(
                rs.getInt("id"),
                rs.getString("user_name"),
                rs.getString("date"),
                rs.getString("start_time"),
                rs.getString("end_time"),
                space
        );
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            refreshSpaceTypesCache();
            return conn.isValid(2);
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            return false;
        }
    }

    static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return connection;
    }

    public void refreshSpaceTypesCache() throws SQLException {
        spaceTypesCache = new HashMap<>();
        String sql = "SELECT id, name FROM CoworkingType";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spaceTypesCache.put(rs.getInt("id"), rs.getString("name"));
            }
        }
    }

    public Map<Integer, String> getAllSpaceTypes() throws SQLException {
        if (spaceTypesCache == null) {
            refreshSpaceTypesCache();
        }
        return spaceTypesCache;
    }

    public String getTypeNameById(int typeId) throws SQLException {
        if (spaceTypesCache == null) {
            refreshSpaceTypesCache();
        }
        return spaceTypesCache.getOrDefault(typeId, "Unknown Type");
    }

    public void addCoworkingSpace(CoworkingSpace space) throws SQLException {
        String sql = "INSERT INTO CoworkingSpace (type_id, price, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, space.getTypeId());
            pstmt.setDouble(2, space.getPrice());
            pstmt.setBoolean(3, space.isAvailable());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    space.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<CoworkingSpace> getCoworkingSpaceById(int id) throws SQLException {
        String sql = "SELECT id, type_id, price, is_available FROM CoworkingSpace WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCoworkingSpace(rs));
                }
            }
        }
        return Optional.empty();
    }

    private CoworkingSpace mapCoworkingSpace(ResultSet rs) throws SQLException {
        return new CoworkingSpace(
                rs.getInt("id"),
                rs.getInt("type_id"),
                rs.getDouble("price"),
                rs.getBoolean("is_available")
        );
    }

    public List<CoworkingSpace> getAllCoworkingSpaces() throws SQLException {
        List<CoworkingSpace> spaces = new ArrayList<>();
        String sql = "SELECT id, type_id, price, is_available FROM CoworkingSpace";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spaces.add(mapCoworkingSpace(rs));
            }
        }
        return spaces;
    }

    public void updateCoworkingSpaceAvailability(int id, boolean isAvailable) throws SQLException {
        String sql = "UPDATE CoworkingSpace SET is_available = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteCoworkingSpace(int id) throws SQLException {
        String sql = "DELETE FROM CoworkingSpace WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Reservation> getReservationsForSpace(int spaceId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT id, user_name, date, start_time, end_time FROM Reservation WHERE space_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, spaceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Optional<CoworkingSpace> space = getCoworkingSpaceById(spaceId);
                    if (space.isPresent()) {
                        reservations.add(new Reservation(
                                rs.getInt("id"),
                                rs.getString("user_name"),
                                rs.getString("date"),
                                rs.getString("start_time"),
                                rs.getString("end_time"),
                                space.get()
                        ));
                    }
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByUser(String userName) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.user_name, r.date, r.start_time, r.end_time, " +
                "r.space_id, cs.type_id, cs.price, cs.is_available " +
                "FROM Reservation r JOIN CoworkingSpace cs ON r.space_id = cs.id " +
                "WHERE r.user_name = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.user_name, r.date, r.start_time, r.end_time, " +
                "cs.id as space_id, cs.type_id, cs.price, cs.is_available " +
                "FROM Reservation r JOIN CoworkingSpace cs ON r.space_id = cs.id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        }
        return reservations;
    }

    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    public void rollbackTransaction() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}