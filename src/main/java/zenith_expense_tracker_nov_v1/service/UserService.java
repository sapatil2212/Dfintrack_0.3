package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.dto.LoginDTO;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.jwt.AuthenticationResponse;

public interface UserService {
    public UserDTO registerUser(UserDTO userDTO);

    AuthenticationResponse loginUser(LoginDTO loginDTO);

    public UserDTO getUserByEmail(String email) throws UserNotFoundException;
    boolean validateSecurityKey(String securityKey);

    boolean checkEmailExists(String email);


}
