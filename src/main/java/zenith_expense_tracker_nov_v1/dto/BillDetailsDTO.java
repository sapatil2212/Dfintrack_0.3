package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDetailsDTO {
    private Long billId;
    private String invoiceNumber;
    private Double totalAmount;
    private LocalDateTime billDateTime;
    private String modeOfPayment;
    private Long bookingId;
    private UserDTO generatedByUser;
    private BookingDTO bookingDetails;
}