package zenith_expense_tracker_nov_v1.dto;

import lombok.*;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDTO {

    private Long id;
    private String billNumber;
    private Long bookingId;
    private String bookingNumber;
    private String guestName;
    private String phoneNumber;
    private String occupancyType;
    private String email;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int numberOfRooms;
    private BigDecimal bookingAmount;
    private BigDecimal totalAmount;
    private BigDecimal foodGSTAmount;
    private BigDecimal stayGSTAmount;
    private LocalDateTime billGenerationDateTime;
    private String paymentMode;
    private BillingType billingType;
    private String bookingStatus;
    private LocalDateTime bookingDateTime;
private BookingStatus bookingStatusEnum;

    private BookingDTO booking;
}