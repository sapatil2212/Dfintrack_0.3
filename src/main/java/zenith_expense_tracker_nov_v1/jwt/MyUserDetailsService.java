package zenith_expense_tracker_nov_v1.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.service.UserService;

import java.util.ArrayList;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserDTO dto = userService.getUserByEmail(email);

            Long propertyId = dto.getPropertyId();
            String propertyName = dto.getPropertyName();

            return new CustomUserDetails(
                    dto.getId(),
                    dto.getEmail(),
                    dto.getName(),
                    dto.getPassword(),
                    dto.getAccountType(),
                    dto.getSecurityKey(),
                    new ArrayList<>(),
                    propertyId,
                    propertyName
            );
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
