package zenith_expense_tracker_nov_v1.api;



import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.service.BookingService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping

    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDto) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDto);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")

    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDto) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDto);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok("Booking deleted successfully with ID: " + id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the booking.");
        }
    }

    @GetMapping("/{id}")

    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO bookingDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping

    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/property/{propertyId}")

    public ResponseEntity<List<BookingDTO>> getBookingsByPropertyId(@PathVariable Long propertyId) {
        List<BookingDTO> bookings = bookingService.getBookingsByPropertyId(propertyId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")

    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/checkout")

    public ResponseEntity<BookingDTO> checkoutBooking(@PathVariable Long id) {
        BookingDTO checkedOutBooking = bookingService.checkoutBooking(id);
        return ResponseEntity.ok(checkedOutBooking);
    }

    @PutMapping("/{id}/status")

    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        BookingDTO updatedBooking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }
}