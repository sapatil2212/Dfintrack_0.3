package zenith_expense_tracker_nov_v1.jwt;


import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}