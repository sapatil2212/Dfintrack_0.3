package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.BillDTO;
import zenith_expense_tracker_nov_v1.entity.Bill;
import zenith_expense_tracker_nov_v1.exception.PaymentDueException;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.service.BillService;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/generate/{bookingId}")
    public ResponseEntity<?> generateBill(@PathVariable Long bookingId) {
        try {
            Bill bill = billService.generateBill(bookingId);
            return ResponseEntity.ok(bill);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PaymentDueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while generating the bill.");
        }
    }

    // New endpoint to view bill details by booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getBillByBookingId(@PathVariable Long bookingId) {
        try {
            BillDTO billDTO = billService.getBillByBookingId(bookingId);
            return ResponseEntity.ok(billDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while fetching the bill.");
        }
    }
    @DeleteMapping("/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable Long billId) {
        try {
            billService.deleteBill(billId);
            return ResponseEntity.ok("Bill deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while deleting the bill.");
        }
    }

}