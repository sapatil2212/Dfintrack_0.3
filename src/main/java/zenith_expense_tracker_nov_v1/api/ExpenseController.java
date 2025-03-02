package zenith_expense_tracker_nov_v1.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.ExpenseDTO;
import zenith_expense_tracker_nov_v1.entity.ArchivedExpenses;
import zenith_expense_tracker_nov_v1.entity.Expenses;
import zenith_expense_tracker_nov_v1.repository.ArchivedExpensesRepository;
import zenith_expense_tracker_nov_v1.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ArchivedExpensesRepository archivedExpensesRepository;
    // Create Expense
    @PostMapping
    public ResponseEntity<Expenses> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        Expenses createdExpense = expenseService.createExpense(expenseDTO);
        return ResponseEntity.ok(createdExpense);
    }
    // Get All Expenses (New method)
    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }
    // Update Expense
    @PutMapping("/{id}")
    public ResponseEntity<Expenses> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        Expenses updatedExpense = expenseService.updateExpense(id, expenseDTO);
        return ResponseEntity.ok(updatedExpense);
    }

    // Delete Expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Get Expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<Expenses> getExpense(@PathVariable Long id) {
        Expenses expense = expenseService.getExpense(id);
        return ResponseEntity.ok(expense);
    }



    @GetMapping("/archived-expenses")
    public List<ArchivedExpenses> getAllArchivedExpenses() {
        return archivedExpensesRepository.findAll();
    }
}