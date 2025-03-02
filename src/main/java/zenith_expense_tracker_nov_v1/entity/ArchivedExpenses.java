package zenith_expense_tracker_nov_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "archived_expenses")
public class ArchivedExpenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "expense_type", nullable = false)
    private String expenseType;

    @Column(name = "generic_expenses", nullable = false)
    private String genericExpenses;

    // No relationships with other tables
}