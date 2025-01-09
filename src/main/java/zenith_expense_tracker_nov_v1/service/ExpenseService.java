package zenith_expense_tracker_nov_v1.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.ExpenseDTO;
import zenith_expense_tracker_nov_v1.entity.Expense;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.exception.UnauthorizedException;
import zenith_expense_tracker_nov_v1.repository.ExpenseRepository;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ExpenseDTO createExpense(ExpenseDTO expenseDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Property property = propertyRepository.findById(expenseDTO.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        Expense expense = new Expense();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setCategory(expenseDTO.getCategory());
        expense.setCreatedBy(user);
        expense.setProperty(property);
        expense.setUser(user);

        // The date will be set automatically by prePersist

        // Save to repository
        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
    }

    public List<ExpenseDTO> getExpenses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesByProperty(Long propertyId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (user.getAccountType() == AccountType.USER && !user.getProperty().equals(property)) {
            throw new UnauthorizedException("User is not authorized to view expenses for this property");
        }

        List<Expense> expenses = expenseRepository.findByPropertyId(propertyId);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesByUser(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getId().equals(currentUserId) && user.getAccountType() != AccountType.ADMIN) {
            throw new UnauthorizedException("User is not authorized to view expenses for this user");
        }

        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO expenseDTO, Long userId, AccountType accountType) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        // Check if the user is either the owner or an admin
        if (!expense.getUser().getId().equals(userId) && accountType != AccountType.ADMIN) {
            throw new UnauthorizedException("User is not authorized to update this expense");
        }

        // Update the expense fields
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setCategory(expenseDTO.getCategory());

        if (expenseDTO.getDate() != null) {
            expense.setDate(expenseDTO.getDate());
        } else {
            expense.setDate(LocalDateTime.now());
        }

        // Save and return the updated expense
        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
    }



    public void deleteExpense(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccountType() == AccountType.ADMIN || expense.getUser().getId().equals(userId)) {
            expenseRepository.delete(expense);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this expense");
        }
    }

    public void deleteAllExpenses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccountType() != AccountType.ADMIN) {
            throw new UnauthorizedException("Only admins can delete all expenses");
        }

        expenseRepository.deleteAll();
    }

    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategory(expense.getCategory());
        dto.setCreatedBy(expense.getCreatedBy().getName());
        dto.setCreatedAt(expense.getCreatedAt());

        if (expense.getProperty() != null) {
            dto.setPropertyId(expense.getProperty().getId());
            dto.setPropertyName(expense.getProperty().getName());
        }

        if (expense.getUser() != null) {
            dto.setUserId(expense.getUser().getId());
            dto.setUserName(expense.getUser().getName());
        }

        return dto;
    }
}