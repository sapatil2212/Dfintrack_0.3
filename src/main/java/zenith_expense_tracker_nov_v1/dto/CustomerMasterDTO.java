package zenith_expense_tracker_nov_v1.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CustomerMasterDTO {
    private Long id;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Company email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String companyEmail;

    @NotBlank(message = "Contact person name is required")
    @Size(min = 2, max = 100, message = "Contact person name must be between 2 and 100 characters")
    private String contactPersonName;

    @NotBlank(message = "Company contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    private String companyContactNumber;

    @Pattern(regexp = "^$|^\\d{10}$", message = "Alternate contact number must be 10 digits if provided")
    private String companyAlternateContactNumber;

    @NotBlank(message = "Company address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String companyAddress;


    @Pattern(regexp = "^$|^NA$|^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[A-Z0-9]{1}[A-Z0-9]{1}[A-Z]{1}[A-Z0-9]{1}$",
            message = "Invalid GST number format. Must be empty, 'NA', or follow the standard Indian GST format")
    private String gstNumber;
}