package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.BankAccountTransactionsDTO;
import zenith_expense_tracker_nov_v1.entity.BankAccountTransactions;
import zenith_expense_tracker_nov_v1.entity.BankAccount;
import zenith_expense_tracker_nov_v1.repository.BankAccountTransactionsRepository;
import zenith_expense_tracker_nov_v1.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankAccountTransactionsService {

    @Autowired
    private BankAccountTransactionsRepository bankAccountTransactionsRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    // Create a new transaction
    @Transactional
    public BankAccountTransactions createTransaction(BankAccountTransactionsDTO transactionDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(transactionDTO.getBankAccountId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        BankAccountTransactions transaction = new BankAccountTransactions();
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(LocalDateTime.now()); // Set the current timestamp
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setBankAccount(bankAccount);

        // The @PrePersist method will automatically set the createdAt field
        return bankAccountTransactionsRepository.save(transaction);
    }

    // Get all transactions for a specific bank account
    public List<BankAccountTransactionsDTO> getTransactionsByBankAccountId(Long bankAccountId) {
        List<BankAccountTransactions> transactions = bankAccountTransactionsRepository.findByBankAccountId(bankAccountId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert BankAccountTransactions entity to BankAccountTransactionsDTO
    private BankAccountTransactionsDTO convertToDTO(BankAccountTransactions transaction) {
        BankAccountTransactionsDTO dto = new BankAccountTransactionsDTO();
        dto.setId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setDescription(transaction.getDescription());
        dto.setBankAccountId(transaction.getBankAccount().getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
    public List<BankAccountTransactionsDTO> getAllTransactions() {
        List<BankAccountTransactions> transactions = bankAccountTransactionsRepository.findAll();
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        BankAccountTransactions transaction = bankAccountTransactionsRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Optionally, you can update the bank account balance if needed
        BankAccount bankAccount = transaction.getBankAccount();
        if ("CREDIT".equalsIgnoreCase(transaction.getTransactionType())) {
            bankAccount.setBalance(bankAccount.getBalance().subtract(transaction.getAmount()));
        } else if ("DEBIT".equalsIgnoreCase(transaction.getTransactionType())) {
            bankAccount.setBalance(bankAccount.getBalance().add(transaction.getAmount()));
        }
        bankAccountRepository.save(bankAccount);

        // Delete the transaction
        bankAccountTransactionsRepository.deleteById(transactionId);
    }

    @Transactional
    public void detachTransactionsFromBankAccount(Long bankAccountId) {
        bankAccountTransactionsRepository.detachTransactionsFromBankAccount(bankAccountId);
    }
}