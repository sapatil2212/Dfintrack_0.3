package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.BillDTO;
import zenith_expense_tracker_nov_v1.entity.BillGenerationRequest;
import zenith_expense_tracker_nov_v1.entity.BillUpdateRequest;
import zenith_expense_tracker_nov_v1.exception.BillAlreadyGeneratedException;
import zenith_expense_tracker_nov_v1.exception.BillFetchException;
import zenith_expense_tracker_nov_v1.exception.BookingNotFoundException;
import zenith_expense_tracker_nov_v1.service.BillService;
import zenith_expense_tracker_nov_v1.utility.ErrorResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateBill(@RequestBody BillGenerationRequest request, @RequestParam Long userId) {
        try {
            if (request.getModeOfPayment() == null || request.getModeOfPayment().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Mode of payment is required", "code", HttpStatus.BAD_REQUEST.value()));
            }

            BillDTO bill = billService.generateBill(request, userId);
            return ResponseEntity.ok(bill);
        } catch (BillAlreadyGeneratedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Bill already generated for this booking", "code", HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred", "code", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getBillByBookingId(@PathVariable Long bookingId) {
        try {
            BillDTO bill = billService.getBillByBookingId(bookingId);
            return ResponseEntity.ok(bill);
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Booking not found", e.getMessage()));
        } catch (BillFetchException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching bill", e.getMessage()));
        }
    }

    @PutMapping("/booking/{bookingId}")
    public ResponseEntity<BillDTO> updateBill(
            @PathVariable Long bookingId,
            @RequestBody BillUpdateRequest updateRequest,
            @RequestParam Long userId) {
        try {
            BillDTO updatedBill = billService.updateBill(bookingId, updateRequest, userId);
            return ResponseEntity.ok(updatedBill);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        try {
            BillDTO bill = billService.getBillById(id);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillDTO>> getBillsByUserId(@PathVariable Long userId) {
        List<BillDTO> bills = billService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BillDTO>> getAllBillsWithUserInfo() {
        try {
            List<BillDTO> bills = billService.getAllBillsWithUserInfo();
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}