package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zenith_expense_tracker_nov_v1.entity.PersonalRevenue;
import java.util.List;

public interface PersonalRevenueRepository extends JpaRepository<PersonalRevenue, Long> {
    List<PersonalRevenue> findByAdminId(Long adminId);
}