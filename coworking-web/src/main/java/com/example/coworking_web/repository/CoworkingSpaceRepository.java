package Repository;

import Data.CoworkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoworkingSpaceRepository extends JpaRepository<CoworkingSpace, Integer> {
    List<CoworkingSpace> findByIsAvailableTrue();
}