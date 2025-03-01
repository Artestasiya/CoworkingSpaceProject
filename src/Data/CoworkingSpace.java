package Data;

import java.io.Serializable;

public class CoworkingSpace implements Serializable {
    private int id;
    private CoworkingType type;
    private double price;
    private boolean isAvailable;

    public CoworkingSpace(int id, CoworkingType type, double price, boolean isAvailable) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
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
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}