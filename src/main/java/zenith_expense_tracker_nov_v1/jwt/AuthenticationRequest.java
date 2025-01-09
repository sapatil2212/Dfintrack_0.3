package zenith_expense_tracker_nov_v1.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationRequest {

private String email;
private String password;
}
