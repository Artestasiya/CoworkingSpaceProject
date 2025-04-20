package Data;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CoworkingType")
public class CoworkingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoworkingSpace> spaces = new ArrayList<>();

    public CoworkingType() {}

    public CoworkingType(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CoworkingSpace> getSpaces() {
        return spaces;
    }

    public void addSpace(CoworkingSpace space) {
        spaces.add(space);
        space.setType(this);
    }

    public void setId(int typeId) {
        this.id = typeId;
    }
}