package zenith_expense_tracker_nov_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import zenith_expense_tracker_nov_v1.enums.BankAccountType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bank_accounts")

public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolderName;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private BankAccountType accountType;

    private String description;

    private String accountNumber;
    private String bankName;
    private String branch;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Expenses> expenses;
}