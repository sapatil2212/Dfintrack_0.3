package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zenith_expense_tracker_nov_v1.entity.BankAccountTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankAccountTransactionsRepository extends JpaRepository<BankAccountTransactions, Long> {
    List<BankAccountTransactions> findByBankAccountId(Long bankAccountId);
    @Modifying
    @Query("UPDATE BankAccountTransactions t SET t.bankAccount = NULL WHERE t.bankAccount.id = :bankAccountId")
    void detachTransactionsFromBankAccount(@Param("bankAccountId") Long bankAccountId);
}