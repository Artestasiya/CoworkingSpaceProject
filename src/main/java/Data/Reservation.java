package Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Reservation")
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 10)
    private String date;

    @Column(name = "start_time", nullable = false, length = 5)
    private String startTime;

    @Column(name = "end_time", nullable = false, length = 5)
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private CoworkingSpace space;

    public Reservation() {
    }

    public Reservation(String userName, String date, String startTime,
                       String endTime, CoworkingSpace space) {
        this.userName = Objects.requireNonNull(userName, "User name cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = Objects.requireNonNull(endTime, "End time cannot be null");
        this.space = Objects.requireNonNull(space, "Space cannot be null");
    }

    public Reservation(int i, String userName, String date, String startTime, String endTime, CoworkingSpace coworkingSpace) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSpace(CoworkingSpace space) {
        this.space = space;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(date, that.date) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(space, that.space);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, date, startTime, endTime, space);
    }

    @Override
    public String toString() {
        return String.format(
                "Reservation[ID: %d, User: %s, Date: %s, Time: %s-%s, Space: %s]",
                id, userName, date, startTime, endTime, space);
    }
}