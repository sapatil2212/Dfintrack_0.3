package zenith_expense_tracker_nov_v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDTO {

    private String username;

    @Email(message = "Invalid email format")
    private String email;

    private String oldPassword;

    private String newPassword;
}
