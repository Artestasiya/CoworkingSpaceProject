package Data;

public enum CoworkingType {
    OPEN_SPACE(1, "Open Space"),
    MEETING_ROOM(2, "Meeting Room"),
    PRIVATE_OFFICE(3, "Private Office");

    private final int id;
    private final String name;

    CoworkingType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CoworkingType getById(int id) {
        for (CoworkingType type : CoworkingType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type id: " + id);
    }
}