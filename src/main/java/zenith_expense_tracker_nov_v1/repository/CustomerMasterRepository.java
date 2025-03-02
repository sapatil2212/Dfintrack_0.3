package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zenith_expense_tracker_nov_v1.entity.CustomerMaster;

@Repository
public interface CustomerMasterRepository extends JpaRepository<CustomerMaster, Long> {
    boolean existsByCompanyEmail(String companyEmail);


    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CustomerMaster c WHERE c.gstNumber = :gstNumber AND c.gstNumber != 'NA'")
    boolean existsByGstNumber(@Param("gstNumber") String gstNumber);
}
