package Data;

import Data.CoworkingSpace;

import java.io.Serializable;

public class Reservation implements Serializable {
    private int id;
    private String userName;
    private String date;
    private String startTime;
    private String endTime;
    private CoworkingSpace space;

    public Reservation(int id, String userName, String date, String startTime, String endTime, CoworkingSpace space) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.space = space;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public CoworkingSpace getSpace() {
        return space;
    }
}