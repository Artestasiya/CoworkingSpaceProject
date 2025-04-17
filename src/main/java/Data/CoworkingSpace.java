package Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CoworkingSpace")
public class CoworkingSpace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private CoworkingType type;

    @Column(nullable = false)
    private double price;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    public CoworkingSpace() {
    }

    public CoworkingSpace(double price) {
        this.price = price;
    }

    public CoworkingSpace(CoworkingType type, double price, boolean available) {
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public CoworkingSpace(int i, int typeId, double price, boolean isAvailable) {
    }

    public int getId() {
        return id;
    }

    public CoworkingType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(CoworkingType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getTypeId() {
        return type != null ? type.getId() : 0;
    }

    @Override
    public String toString() {
        return String.format("Space ID: %d, Type: %s, Price: %.2f, Available: %s",
                id, type != null ? type.toString() : "null", price, available ? "Yes" : "No");
    }
}