package zenith_expense_tracker_nov_v1.repository;

import zenith_expense_tracker_nov_v1.entity.ArchivedExpenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedExpensesRepository extends JpaRepository<ArchivedExpenses, Long> {
}