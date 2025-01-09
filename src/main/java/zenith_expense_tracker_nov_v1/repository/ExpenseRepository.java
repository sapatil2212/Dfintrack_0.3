package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.Expense;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByPropertyId(Long propertyId);

    List<Expense> findByUserId(Long userId);

    List<Expense> findByPropertyIdAndCreatedBy_Id(Long propertyId, Long createdById);

}
