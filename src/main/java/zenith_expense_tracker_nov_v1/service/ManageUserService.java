package zenith_expense_tracker_nov_v1.service;


import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.UserDTO;

import java.util.List;

public interface ManageUserService {

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByAccountType(AccountType accountType);

    List<UserDTO> getAdminsWithoutPassword();

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
