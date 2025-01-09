package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.Booking;

import java.time.LocalDate;
import java.util.List;



@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPropertyId(Long propertyId);
    List<Booking> findByUserId(Long userId);
    Booking save(Booking booking);
}
