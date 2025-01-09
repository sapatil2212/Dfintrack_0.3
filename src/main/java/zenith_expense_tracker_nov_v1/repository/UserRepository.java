package zenith_expense_tracker_nov_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long > {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByAccountType(AccountType accountType);
    List<User> findByProperty(Property property);
    Optional<User> findByName(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.property WHERE u.email = :email")
    Optional<User> findByEmailWithProperty(@Param("email") String email);

}
