package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillAndBookingDTO {

    private Long billId;
    private BigDecimal totalAmount;
    private LocalDateTime billDateTime;
    private String paymentStatus;
    private String paymentMethod;
    private String additionalCharges;
    private String notes;

    private Long bookingId;
    private String guestName;
    private String email;
    private String phoneNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomNo;
    private int noOfGuests;
    private int noOfChildren;
    private String companyName;
    private boolean isCheckedOut;
    private String documentSubmitted;
    private String createdByUsername;
}