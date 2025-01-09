package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BillGenerationDTO {
    private Long bookingId;
    private BigDecimal totalAmount;
}

