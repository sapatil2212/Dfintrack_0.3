package zenith_expense_tracker_nov_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CustomerMaster")
public class CustomerMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "company_email", nullable = false, unique = true, length = 100)
    private String companyEmail;

    @Column(name = "contact_person_name", nullable = false, length = 100)
    private String contactPersonName;

    @Column(name = "company_contact_number", nullable = false, length = 15)
    private String companyContactNumber;

    @Column(name = "company_alternate_contact_number", length = 15)
    private String companyAlternateContactNumber;

    @Column(name = "company_address", nullable = false, length = 255)
    private String companyAddress;

    @Column(name = "gst_number", nullable = false, unique = true, length = 15)
    private String gstNumber= "NA";

}
