package zenith_expense_tracker_nov_v1.jwt;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.entity.Property;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private AccountType accountType;

    private String securityKey;
    private Collection<? extends GrantedAuthority> authorities;

    private Long propertyId;
    private String propertyName;

    // Single constructor that initializes all fields, including property details
    public CustomUserDetails(Long id, String email, String name, String password,
                             AccountType accountType, String securityKey,
                             Collection<? extends GrantedAuthority> authorities, Long propertyId, String propertyName) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.accountType = accountType;
        this.securityKey = securityKey;
        this.authorities = authorities;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
