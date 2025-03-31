package Data;

import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoworkingSpace implements Serializable {
    private int id;
    private final int typeId;
    private final double price;
    private boolean isAvailable;

    public CoworkingSpace(int id, int typeId, double price, boolean isAvailable) {
        this.id = id;
        this.typeId = typeId;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public int getTypeId() { return typeId; }
    public double getPrice() { return price; }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("Space ID: %d, Type ID: %d, Price: %.2f, Available: %s",
                id, typeId, price, isAvailable ? "Yes" : "No");
    }

    public CoworkingType getType() throws SQLException {
        String sql = "SELECT id, name FROM CoworkingType WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.typeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CoworkingType(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        throw new SQLException("Coworking type not found for ID: " + this.typeId);
    }
}