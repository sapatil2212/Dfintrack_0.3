package zenith_expense_tracker_nov_v1.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankAccountTransactionsDTO {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
    private Long bankAccountId;
    private LocalDateTime createdAt;
}