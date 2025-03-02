package zenith_expense_tracker_nov_v1.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import zenith_expense_tracker_nov_v1.dto.BankAccountDTO;
import zenith_expense_tracker_nov_v1.dto.BankAccountTransactionsDTO;
import zenith_expense_tracker_nov_v1.entity.BankAccount;
import zenith_expense_tracker_nov_v1.exception.BankAccountNotFoundException;

import zenith_expense_tracker_nov_v1.service.BankAccountService;
import zenith_expense_tracker_nov_v1.service.BankAccountTransactionsService;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@Validated
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountTransactionsService bankAccountTransactionsService;

    @PostMapping("/add")
    public ResponseEntity<BankAccount> addBankAccount(@RequestBody @Valid BankAccountDTO dto) {
        BankAccount account = bankAccountService.addBankAccount(dto);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id) {
        BankAccount account = bankAccountService.getBankAccountById(id);
        if (account == null) {
            throw new BankAccountNotFoundException("Bank Account with id " + id + " not found");
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long id, @RequestBody @Valid BankAccountDTO dto) {
        BankAccount updatedAccount = bankAccountService.updateBankAccount(id, dto);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBankAccount(@PathVariable Long id) {
        BankAccount account = bankAccountService.getBankAccountById(id);
        if (account == null) {
            throw new BankAccountNotFoundException("Bank Account with id " + id + " not found");
        }
        bankAccountService.deleteBankAccount(id);
        return new ResponseEntity<>("Bank Account deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/passbook/{bankAccountId}")
    public ResponseEntity<List<BankAccountTransactionsDTO>> getTransactionsByBankAccountId(@PathVariable Long bankAccountId) {
        List<BankAccountTransactionsDTO> transactions = bankAccountTransactionsService.getTransactionsByBankAccountId(bankAccountId);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<BankAccountTransactionsDTO>> getAllTransactions() {
        List<BankAccountTransactionsDTO> transactions = bankAccountTransactionsService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        bankAccountTransactionsService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}


