package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;

import zenith_expense_tracker_nov_v1.enums.BankAccountType;

import java.math.BigDecimal;


@Data
public class BankAccountDTO {
    private String accountHolderName;
    private BankAccountType accountType;
    private String description;
    private BigDecimal initialBalance;
    private String accountNumber;
    private String bankName;
    private String branch;
}