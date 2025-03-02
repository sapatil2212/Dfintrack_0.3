package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.CustomerMasterDTO;
import zenith_expense_tracker_nov_v1.exception.CustomerAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.InvalidGstNumberException;
import zenith_expense_tracker_nov_v1.service.CustomerMasterService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/customers")
public class CustomerMasterController {

    @Autowired
    private CustomerMasterService customerMasterService;

    public CustomerMasterController(CustomerMasterService customerMasterService) {
        this.customerMasterService = customerMasterService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerMasterDTO customerMasterDTO) {
        try {
            // Try to create the customer
            CustomerMasterDTO createdCustomer = customerMasterService.createCustomer(customerMasterDTO);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED); // 201 Created
        } catch (CustomerAlreadyExistsException e) {
            // Email already exists, return 409 Conflict with a message
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT); // 409 Conflict
        } catch (InvalidGstNumberException e) {
            // Invalid GST number, return 400 Bad Request with a message
            return new ResponseEntity<>("Invalid GST Number format", HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            // General exception handler (optional, you may customize this)
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerMasterDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerMasterDTO customerMasterDTO) {
        CustomerMasterDTO updatedCustomer = customerMasterService.updateCustomer(id, customerMasterDTO);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerMasterService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerMasterDTO> getCustomerById(@PathVariable Long id) {
        CustomerMasterDTO customer = customerMasterService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CustomerMasterDTO>> getAllCustomers() {
        List<CustomerMasterDTO> customers = customerMasterService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
