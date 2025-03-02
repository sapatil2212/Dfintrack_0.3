package zenith_expense_tracker_nov_v1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import zenith_expense_tracker_nov_v1.entity.Bill;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    boolean existsByBookingId(Long bookingId);
    Optional<Bill> findByBookingId(Long bookingId);
    Optional<Bill> findByBookingIdAndBookingStatusEnum(Long bookingId, BookingStatus bookingStatus);
    Optional<Bill> findTopByBookingIdOrderByBillGenerationDateTimeDesc(Long bookingId);
}