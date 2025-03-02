package zenith_expense_tracker_nov_v1.dto;

import zenith_expense_tracker_nov_v1.entity.Expenses;
import java.time.LocalDateTime;

public class ExpenseDTO {
    private Long id;
    private Long amount;
    private String expenseType;
    private String genericExpenses;
    private Long propertyId;
    private Long bankAccountId;
    private String propertyName;
    private String bankAccountName;
    private String description;
    private LocalDateTime date;
    private String createdBy;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getGenericExpenses() {
        return genericExpenses;
    }

    public void setGenericExpenses(String genericExpenses) {
        this.genericExpenses = genericExpenses;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Static Conversion Method
    public static ExpenseDTO convertToDTO(Expenses expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setExpenseType(expense.getExpenseType());
        dto.setGenericExpenses(expense.getGenericExpenses());

        // Fetch property name properly
        if (expense.getProperty() != null) {
            dto.setPropertyId(expense.getProperty().getId());
            dto.setPropertyName(expense.getProperty().getName());
        } else {
            dto.setPropertyId(null);
            dto.setPropertyName(expense.getPropertyName()); // Fetch from column
        }

        // Handle bank account details
        if (expense.getBankAccount() != null) {
            dto.setBankAccountId(expense.getBankAccount().getId());
            dto.setBankAccountName(expense.getBankAccount().getAccountHolderName());
        } else {
            dto.setBankAccountId(null);
            dto.setBankAccountName("No Bank Account");
        }

        dto.setDescription(expense.getDescription());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setDate(expense.getDate());
        dto.setCreatedBy(expense.getCreatedBy());

        return dto;
    }


}
