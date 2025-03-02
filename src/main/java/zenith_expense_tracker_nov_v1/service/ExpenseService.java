package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.BankAccountTransactionsDTO;
import zenith_expense_tracker_nov_v1.dto.ExpenseDTO;
import zenith_expense_tracker_nov_v1.entity.Expenses;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.BankAccount;
import zenith_expense_tracker_nov_v1.repository.ExpenseRepository;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountTransactionsService bankAccountTransactionsService;

    @Transactional
    public Expenses createExpense(ExpenseDTO expenseDTO) {
        Optional<Property> property = propertyRepository.findById(expenseDTO.getPropertyId());
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(expenseDTO.getBankAccountId());

        if (property.isPresent() && bankAccount.isPresent()) {
            // Deduct amount from bank account
            bankAccountService.deductExpenseFromAccount(bankAccount.get(),
                    BigDecimal.valueOf(expenseDTO.getAmount()));

            // Create the expense
            Expenses expense = new Expenses();
            expense.setAmount(expenseDTO.getAmount());
            expense.setExpenseType(expenseDTO.getExpenseType());
            expense.setGenericExpenses(expenseDTO.getGenericExpenses());
            expense.setProperty(property.get());
            expense.setPropertyName(property.get().getName());
            expense.setBankAccount(bankAccount.get());
            expense.setCreatedAt(LocalDateTime.now());
            expense.setDate(LocalDateTime.now());
            expense.setDescription(expenseDTO.getDescription()); // Use the description from DTO
            expense.setCreatedBy(expenseDTO.getCreatedBy());

            // Save the expense
            Expenses savedExpense = expenseRepository.save(expense);

            // Create a transaction with detailed description
            String transactionDescription = String.format(
                    "Expense: %s, Amount: %s, Property: %s, Description: %s",
                    expenseDTO.getExpenseType(),
                    expenseDTO.getAmount(),
                    property.get().getName(),
                    expenseDTO.getDescription()
            );

            BankAccountTransactionsDTO transactionDTO = new BankAccountTransactionsDTO();
            transactionDTO.setTransactionType("DEBIT");
            transactionDTO.setAmount(BigDecimal.valueOf(expenseDTO.getAmount()));
            transactionDTO.setTransactionDate(LocalDateTime.now());
            transactionDTO.setDescription(transactionDescription);
            transactionDTO.setBankAccountId(expenseDTO.getBankAccountId());

            // Save the transaction
            bankAccountTransactionsService.createTransaction(transactionDTO);

            return savedExpense;
        } else {
            throw new RuntimeException("Property or Bank Account not found!");
        }
    }
    public Expenses updateExpense(Long id, ExpenseDTO expenseDTO) {
        Optional<Expenses> existingExpense = expenseRepository.findById(id);
        if (existingExpense.isPresent()) {
            Expenses expense = existingExpense.get();
            expense.setAmount(expenseDTO.getAmount());
            expense.setExpenseType(expenseDTO.getExpenseType());
            expense.setGenericExpenses(expenseDTO.getGenericExpenses());
            // Add this line to update the createdBy field
            expense.setCreatedBy(expenseDTO.getCreatedBy());

            Optional<Property> property = propertyRepository.findById(expenseDTO.getPropertyId());
            Optional<BankAccount> bankAccount = bankAccountRepository.findById(expenseDTO.getBankAccountId());

            if (property.isPresent()) {
                expense.setProperty(property.get());
                expense.setPropertyName(property.get().getName());
            }
            if (bankAccount.isPresent()) {
                expense.setBankAccount(bankAccount.get());
            }

            return expenseRepository.save(expense);
        } else {
            throw new RuntimeException("Expense not found!");
        }
    }

    @Transactional
    public void deleteExpense(Long id) {
        // Retrieve the expense first
        Expenses expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found!"));

        // Get the associated bank account
        BankAccount bankAccount = expense.getBankAccount();

        // Revert the amount back to the bank account
        BigDecimal expenseAmount = BigDecimal.valueOf(expense.getAmount());
        bankAccount.setBalance(bankAccount.getBalance().add(expenseAmount));
        bankAccountRepository.save(bankAccount);

        // Delete the expense
        expenseRepository.deleteById(id);
    }

    public Expenses getExpense(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found!"));
    }

    public List<ExpenseDTO> getAllExpenses() {
        List<Expenses> expenses = expenseRepository.findAll();
        return expenses.stream()
                .map(ExpenseDTO::convertToDTO)
                .collect(Collectors.toList());
    }


}
