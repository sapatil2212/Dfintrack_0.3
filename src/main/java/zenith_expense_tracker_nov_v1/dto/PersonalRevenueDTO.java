package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PersonalRevenueDTO {
    private Long id;
    private BigDecimal amount;
    private String revenueType;
    private LocalDateTime dateTime;
    private String bank;
    private String paymentMode;
    private String description;
    private Long adminId;
    private String adminName;
}