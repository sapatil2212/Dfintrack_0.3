package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.Booking;

import java.util.List;
import java.util.Optional;


public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDto);
    BookingDTO updateBooking(Long id, BookingDTO bookingDto);
    void deleteBooking(Long id);
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getAllBookings();
    List<BookingDTO> getBookingsByPropertyId(Long propertyId);
    List<BookingDTO> getBookingsByUserId(Long userId);
    BookingDTO checkoutBooking(Long id);
    BookingDTO updateBookingStatus(Long id, String status);


}