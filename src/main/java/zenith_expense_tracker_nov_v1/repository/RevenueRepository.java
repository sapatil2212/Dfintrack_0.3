package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zenith_expense_tracker_nov_v1.entity.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {
}