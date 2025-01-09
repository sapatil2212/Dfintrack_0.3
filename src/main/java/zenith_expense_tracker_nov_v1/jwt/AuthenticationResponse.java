package zenith_expense_tracker_nov_v1.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data

public class AuthenticationResponse {
    private String jwtToken;
    private String refreshToken;
    private String tokenType = "Bearer";


    public AuthenticationResponse(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;

    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}