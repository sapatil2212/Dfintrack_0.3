package zenith_expense_tracker_nov_v1.entity;

import jakarta.persistence.*;
import lombok.*;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.enums.BookingType;
import zenith_expense_tracker_nov_v1.enums.OccupancyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", unique = true, nullable = false)
    private String bookingNumber;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "payment_mode", nullable = false)
    private String paymentMode;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    @ManyToOne
    @JoinColumn(name = "customer_master_id")
    private CustomerMaster customerMaster;

    // Fields to store customer details (for corporate bookings)
    @Column(name = "customer_company_name", length = 100)
    private String customerCompanyName;

    @Column(name = "customer_company_email", length = 100)
    private String customerCompanyEmail;

    @Column(name = "customer_contact_person_name", length = 100)
    private String customerContactPersonName;

    @Column(name = "customer_company_contact_number", length = 15)
    private String customerCompanyContactNumber;

    @Column(name = "customer_company_address", length = 255)
    private String customerCompanyAddress;

    @Column(name = "customer_gst_number", length = 15)
    private String customerGstNumber;

    private String guestName;
    private String phoneNumber;
    private int numberOfRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private OccupancyType occupancyType;

    private String idProof;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private BigDecimal bookingAmount;
    private BigDecimal advanceAmount;
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    private BigDecimal totalBillAmount;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;

    @Column(nullable = false)
    private String email;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal foodGSTAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal stayGSTAmount;

    // Add getters and setters
    public BigDecimal getFoodGSTAmount() {
        return foodGSTAmount;
    }

    public void setFoodGSTAmount(BigDecimal foodGSTAmount) {
        this.foodGSTAmount = foodGSTAmount;
    }

    public BigDecimal getStayGSTAmount() {
        return stayGSTAmount;
    }

    public void setStayGSTAmount(BigDecimal stayGSTAmount) {
        this.stayGSTAmount = stayGSTAmount;
    }
}