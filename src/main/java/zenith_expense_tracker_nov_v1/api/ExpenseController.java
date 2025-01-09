package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.ExpenseDTO;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.exception.UnauthorizedException;
import zenith_expense_tracker_nov_v1.jwt.CustomUserDetails;
import zenith_expense_tracker_nov_v1.service.ExpenseService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Check if the user is ADMIN, only ADMIN can view all expenses
            if (userDetails.getAccountType() != AccountType.ADMIN) {
                return ResponseEntity.status(403).body(null);
            }

            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ExpenseDTO createdExpense = expenseService.createExpense(expenseDTO, userDetails.getId());
            return ResponseEntity.ok(createdExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ExpenseDTO("Error creating expense: " + e.getMessage()));
        }
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesForProperty(@PathVariable Long propertyId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<ExpenseDTO> expenses = expenseService.getExpensesByProperty(propertyId, userDetails.getId());
            return ResponseEntity.ok(expenses);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(expenseService.getExpenses(userDetails.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseDTO expenseDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            ExpenseDTO updatedExpense = expenseService.updateExpense(
                    expenseId,
                    expenseDTO,
                    userDetails.getId(),
                    userDetails.getAccountType()
            );
            return ResponseEntity.ok(updatedExpense);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Expense Not Found",
                    "message", e.getMessage()
            ));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Unauthorized Access",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "An unexpected error occurred. Please try again later."
            ));
        }
    }


    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Delete expense based on user role
            expenseService.deleteExpense(expenseId, userDetails.getId());
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("Expense not found");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body("Unauthorized to delete this expense");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting expense: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllExpenses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            expenseService.deleteAllExpenses(userDetails.getId());
            return ResponseEntity.ok("All expenses deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting all expenses: " + e.getMessage());
        }
    }
}
