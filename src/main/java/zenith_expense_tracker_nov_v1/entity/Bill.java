package zenith_expense_tracker_nov_v1.entity;

import jakarta.persistence.*;
import lombok.*;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.enums.BookingType;
import zenith_expense_tracker_nov_v1.enums.OccupancyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private OccupancyType occupancyType;

    @Column(nullable = false)
    private String bookingNumber;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime checkInDate;

    @Column(nullable = false)
    private LocalDateTime checkOutDate;

    @Column(nullable = false)
    private int numberOfRooms;

    @Column(nullable = false)
    private BigDecimal bookingAmount;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal foodGSTAmount;

    @Column(nullable = false)
    private BigDecimal stayGSTAmount;

    @Column(nullable = false)
    private LocalDateTime billGenerationDateTime;

    @Column(nullable = false)
    private String paymentMode;

    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatusEnum;

    @Lob
    private byte[] pdfContent;
}