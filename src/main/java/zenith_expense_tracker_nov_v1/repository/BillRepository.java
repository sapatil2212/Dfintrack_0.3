package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.Bill;


import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBookingId(Long bookingId);
    List<Bill> findByGeneratedByUserId(Long userId);
    boolean existsByInvoiceNumber(String invoiceNumber);
    boolean existsByBookingId(Long bookingId);
    List<Bill> findByPropertyName(String propertyName);
    List<Bill> findByPropertyId(Long PropertyId);
    Bill save(Bill bill);

}
