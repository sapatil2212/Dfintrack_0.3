package zenith_expense_tracker_nov_v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long id;

    private String username;

    @Email
    private String email;

    private String accountType;

    private String propertyName;

    private String securityKey;
}
