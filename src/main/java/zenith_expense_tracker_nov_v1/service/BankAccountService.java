package zenith_expense_tracker_nov_v1.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.BankAccountDTO;
import zenith_expense_tracker_nov_v1.dto.BankAccountTransactionsDTO;
import zenith_expense_tracker_nov_v1.entity.BankAccount;
import zenith_expense_tracker_nov_v1.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountTransactionsService bankAccountTransactionsService;

    @Transactional
    public BankAccount addBankAccount(BankAccountDTO dto) {
        BankAccount account = BankAccount.builder()
                .accountHolderName(dto.getAccountHolderName())
                .accountType(dto.getAccountType())
                .description(dto.getDescription())
                .balance(dto.getInitialBalance())
                .accountNumber(dto.getAccountNumber()) // New field
                .bankName(dto.getBankName())         // New field
                .branch(dto.getBranch())             // New field
                .build();
        return bankAccountRepository.save(account);
    }

    @Transactional
    public void deductExpenseFromAccount(BankAccount bankAccount, BigDecimal expenseAmount) {
        if (bankAccount.getBalance().compareTo(expenseAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        bankAccount.setBalance(bankAccount.getBalance().subtract(expenseAmount));
        bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void addToAccountBalance(BankAccount bankAccount, BigDecimal amount) {
        bankAccount.setBalance(bankAccount.getBalance().add(amount));
        bankAccountRepository.save(bankAccount);

        // Create a CREDIT transaction
        BankAccountTransactionsDTO transactionDTO = new BankAccountTransactionsDTO();
        transactionDTO.setTransactionType("CREDIT");
        transactionDTO.setAmount(amount);
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setDescription("Amount Added");
        transactionDTO.setBankAccountId(bankAccount.getId());
        bankAccountTransactionsService.createTransaction(transactionDTO);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));
    }

    @Transactional
    public void deleteBankAccount(Long id) {
        // Fetch the bank account
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        // Set the bank account reference to NULL in all related transactions
        bankAccountTransactionsService.detachTransactionsFromBankAccount(id);

        // Now delete the bank account
        bankAccountRepository.deleteById(id);
    }

    @Transactional
    public BankAccount updateBankAccount(Long id, BankAccountDTO dto) {
        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        existingAccount.setAccountHolderName(dto.getAccountHolderName());
        existingAccount.setAccountType(dto.getAccountType());
        existingAccount.setDescription(dto.getDescription());

        // Update balance if it's provided in the request
        if (dto.getInitialBalance() != null) {
            existingAccount.setBalance(dto.getInitialBalance());
        }

        // Update new fields
        existingAccount.setAccountNumber(dto.getAccountNumber());
        existingAccount.setBankName(dto.getBankName());
        existingAccount.setBranch(dto.getBranch());

        return bankAccountRepository.save(existingAccount);
    }
}