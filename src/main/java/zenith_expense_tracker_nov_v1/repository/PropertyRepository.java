package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.Property;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByUserId(Long userId);
}