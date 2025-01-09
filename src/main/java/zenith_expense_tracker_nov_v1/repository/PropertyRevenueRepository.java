package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zenith_expense_tracker_nov_v1.entity.PropertyRevenue;
import java.util.List;

public interface PropertyRevenueRepository extends JpaRepository<PropertyRevenue, Long> {
    List<PropertyRevenue> findByPropertyId(Long propertyId);
    List<PropertyRevenue> findByUserId(Long userId);
}