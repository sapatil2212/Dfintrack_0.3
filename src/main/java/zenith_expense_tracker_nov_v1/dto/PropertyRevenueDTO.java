package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PropertyRevenueDTO {
    private Long id;
    private BigDecimal amount;
    private String source;
    private LocalDateTime dateTime;
    private Long propertyId;
    private String propertyName;
    private Long userId;
    private String userName;
    private String description;
    private String paymentMode;
    private String billInvoiceNumber;
}