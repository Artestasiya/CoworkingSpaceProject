package Data;

import java.io.Serializable;
import java.util.Objects;

public final class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private final String userName;
    private final String date;
    private final String startTime;
    private final String endTime;
    private final CoworkingSpace space;

    public Reservation(int id, String userName, String date,
                       String startTime, String endTime, CoworkingSpace space) {
        this.id = id;
        this.userName = Objects.requireNonNull(userName, "User name cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = Objects.requireNonNull(endTime, "End time cannot be null");
        this.space = Objects.requireNonNull(space, "Coworking space cannot be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int id;
        private String userName;
        private String date;
        private String startTime;
        private String endTime;
        private CoworkingSpace space;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder space(CoworkingSpace space) {
            this.space = space;
            return this;
        }

        public Reservation build() {
            return new Reservation(id, userName, date, startTime, endTime, space);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                userName.equals(that.userName) &&
                date.equals(that.date) &&
                startTime.equals(that.startTime) &&
                endTime.equals(that.endTime) &&
                space.equals(that.space);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, date, startTime, endTime, space);
    }

    @Override
    public String toString() {
        return String.format(
                "Reservation[ID: %d, User: %s, Date: %s, Time: %s-%s, Space: %s]",
                id, userName, date, startTime, endTime, space
        );
    }
}