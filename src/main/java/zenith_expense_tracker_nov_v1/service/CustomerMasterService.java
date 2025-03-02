package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.CustomerMasterDTO;
import zenith_expense_tracker_nov_v1.exception.CustomerAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.InvalidGstNumberException;

import java.util.List;

public interface CustomerMasterService {
    CustomerMasterDTO createCustomer(CustomerMasterDTO customerMasterDTO) throws CustomerAlreadyExistsException, InvalidGstNumberException;
    CustomerMasterDTO updateCustomer(Long id, CustomerMasterDTO customerMasterDTO);
    void deleteCustomer(Long id);
    CustomerMasterDTO getCustomerById(Long id);
    List<CustomerMasterDTO> getAllCustomers();
}