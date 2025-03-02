package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.Booking;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.service.BookingService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDTO) {

        if (bookingDTO.getBookingAmount() == null || bookingDTO.getBookingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Booking amount must be greater than zero.");
        }


        Booking booking = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(booking);
    }

    // Cancel a booking
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long id,
            @RequestParam BigDecimal refundAmount) {
        // Validate refund amount (optional)
        if (refundAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Refund amount cannot be negative.");
        }

        // Delegate to the service layer
        bookingService.cancelBooking(id, refundAmount);
        return ResponseEntity.noContent().build();
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmBooking(
            @PathVariable Long id,
            @RequestParam BigDecimal remainingAmount) {
        try {
            // Validate remaining amount (must be between 0 and the total booking amount)
            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Remaining amount cannot be negative.");
            }

            // Delegate to the service layer
            Booking booking = bookingService.confirmBooking(id, remainingAmount);
            return ResponseEntity.ok(booking);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("An error occurred while confirming the booking.");
        }
    }
    // Update a booking
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingDTO bookingDTO) {
        // Validate the bookingDTO
        if (bookingDTO.getBookingAmount() == null || bookingDTO.getBookingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Booking amount must be greater than zero.");
        }

        // Validate GST related fields
        if (bookingDTO.getBillingType() == BillingType.GST) {
            if (bookingDTO.getAcceptFoodGST() == null) {
                throw new IllegalArgumentException("Food GST preference must be specified for GST billing type.");
            }
        }

        // Validate booking status transitions
        if (bookingDTO.getBookingStatus() == BookingStatus.CONFIRMED) {
            if (bookingDTO.getAdvanceAmount().compareTo(bookingDTO.getBookingAmount()) < 0) {
                throw new IllegalArgumentException("Advance amount must equal booking amount for confirmed bookings.");
            }
        }

        // Delegate to the service layer
        Booking updatedBooking = bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.ok(updatedBooking);
    }

    // Delete a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    // Delete all bookings
    @DeleteMapping
    public ResponseEntity<Void> deleteAllBookings() {
        bookingService.deleteAllBookings();
        return ResponseEntity.noContent().build();
    }
}