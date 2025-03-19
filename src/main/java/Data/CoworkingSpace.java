package Data;

import java.io.Serializable;

public class CoworkingSpace implements Serializable {
    private int id;
    private Data.CoworkingType type;
    private double price;
    private boolean isAvailable;

    public CoworkingSpace(int id, Data.CoworkingType type, double price, boolean isAvailable) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public Data.CoworkingType getType() {
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