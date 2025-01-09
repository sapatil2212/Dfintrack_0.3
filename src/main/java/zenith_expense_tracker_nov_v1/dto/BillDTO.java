package zenith_expense_tracker_nov_v1.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDTO {
    private Long id;
    private String invoiceNumber;
    private Long bookingId;
    private Double amount;
    private Double totalAmount;
    private Double cgst;
    private Double sgst;
    private LocalDateTime billDateTime;
    private LocalDateTime generatedDate;
    private Long generatedByUserId;
    private String generatedByUsername;
    private String propertyName;
    private Long propertyId;
    private BookingDTO bookingDetails;
    private String modeOfPayment;

    private String generatedByUserEmail;
    private AccountType generatedByUserType;
    private Long generatedByUserPropertyId;
    private String generatedByUserPropertyName;
}





