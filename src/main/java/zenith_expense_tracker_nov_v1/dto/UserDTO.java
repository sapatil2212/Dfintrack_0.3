package zenith_expense_tracker_nov_v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zenith_expense_tracker_nov_v1.entity.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "{NotBlank.name}")
    private String name;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;

    @NotBlank(message = "{NotBlank.password}")

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}\\[\\]|\\\\:;\\\"'<>,.?/-]).{5,15}$", message = "{Pattern.password}")
    private String password;

    private AccountType accountType;

    private LocalDateTime registrationDate;

    @NotBlank(message = "{NotBlank.securityKey}")
    private String securityKey;

    private Long propertyId;

    private String propertyName;

    // Convert UserDTO to User entity
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setAccountType(this.accountType);
        user.setRegistrationDate(this.registrationDate);
        user.setSecurityKey(this.securityKey);
        return user;
    }

    // Convert User entity to UserDTO
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAccountType(),
                user.getRegistrationDate(),
                user.getSecurityKey(),
                user.getProperty() != null ? user.getProperty().getId() : null,
                user.getProperty() != null ? user.getProperty().getName() : null
        );
    }
}
