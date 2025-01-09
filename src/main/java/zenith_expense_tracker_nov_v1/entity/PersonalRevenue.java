package zenith_expense_tracker_nov_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "personal_revenues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String revenueType; // SALARY, INVESTMENT, BUSINESS, OTHER

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String paymentMode;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
}