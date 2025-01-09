package zenith_expense_tracker_nov_v1.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String invoiceNumber;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "booking_id", nullable = false)
        private Booking booking;

        @Column(nullable = false)
        private Double amount;

        @Column(nullable = false)
        private Double totalAmount;

        @Column(nullable = false)
        private Double cgst;

        @Column(nullable = false)
        private Double sgst;

        @Column(name = "bill_date_time", nullable = false)
        private LocalDateTime billDateTime;

        @Column(nullable = false)
        private LocalDateTime generatedDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "generated_by_user_id", nullable = false)
        private User generatedByUser;

        @Column(nullable = false)
        private String propertyName;

        @Column
        private Long propertyId;

    @Column(nullable = false)
    private String modeOfPayment;

    // Add getters and setters
    public LocalDateTime getBillDateTime() {
        return billDateTime;
    }

    public void setBillDateTime(LocalDateTime billDateTime) {
        this.billDateTime = billDateTime;
    }

    // Other getters and setters remain the same
}
