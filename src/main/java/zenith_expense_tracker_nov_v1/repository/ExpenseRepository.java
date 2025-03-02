package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.Expenses;

@Repository
public interface ExpenseRepository extends JpaRepository<Expenses, Long> {
    // The methods findById, save, and deleteById are provided by JpaRepository
}