package zenith_expense_tracker_nov_v1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.CustomerMasterDTO;
import zenith_expense_tracker_nov_v1.entity.Booking;
import zenith_expense_tracker_nov_v1.entity.CustomerMaster;
import zenith_expense_tracker_nov_v1.exception.CustomerAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.InvalidGstNumberException;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.repository.BookingRepository;
import zenith_expense_tracker_nov_v1.repository.CustomerMasterRepository;
import zenith_expense_tracker_nov_v1.validator.GstValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerMasterServiceImpl implements CustomerMasterService {

    @Autowired
    private CustomerMasterRepository customerMasterRepository;

    @Autowired
    private GstValidator gstValidator;

    @Autowired
    private BookingRepository bookingRepository;
    @Override
    @Transactional
    public CustomerMasterDTO createCustomer(CustomerMasterDTO customerMasterDTO) {
        log.info("Starting customer creation process with GST: {}", customerMasterDTO.getGstNumber());

        // Check if email is empty or null
        if (customerMasterDTO.getCompanyEmail() == null || customerMasterDTO.getCompanyEmail().isEmpty()) {
            log.error("Email is empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Handle GST number - allow null or "NA"
        if (customerMasterDTO.getGstNumber() == null ||
                customerMasterDTO.getGstNumber().trim().isEmpty() ||
                customerMasterDTO.getGstNumber().equalsIgnoreCase("NA")) {
            // Set GST number to "NA"
            customerMasterDTO.setGstNumber("NA");
        } else {
            // Check if the provided non-NA GST number already exists
            if (customerMasterRepository.existsByGstNumber(customerMasterDTO.getGstNumber())) {
                log.error("Customer with GST number {} already exists", customerMasterDTO.getGstNumber());
                throw new CustomerAlreadyExistsException("GST number already exists");
            }

            // Validate GST format
            boolean isGstValid = gstValidator.isValid(customerMasterDTO.getGstNumber());
            log.debug("GST Validation result for {} is: {}", customerMasterDTO.getGstNumber(), isGstValid);

            if (!isGstValid) {
                log.error("GST validation failed for number: {}", customerMasterDTO.getGstNumber());
                throw new InvalidGstNumberException("Invalid GST Number format. GST should be in format: 22AAAAA0000A1Z5");
            }
        }

        // Check if email already exists
        if (customerMasterRepository.existsByCompanyEmail(customerMasterDTO.getCompanyEmail())) {
            log.error("Customer with email {} already exists", customerMasterDTO.getCompanyEmail());
            throw new CustomerAlreadyExistsException("Email already exists");
        }

        // Proceed to save the customer if no errors
        CustomerMaster customer = mapToEntity(customerMasterDTO);
        customer = customerMasterRepository.save(customer);
        log.info("Customer created successfully with ID: {}", customer.getId());

        return mapToDTO(customer);
    }


    // Update customer
    @Override
    public CustomerMasterDTO updateCustomer(Long id, CustomerMasterDTO customerMasterDTO) {
        CustomerMaster existingCustomer = customerMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Update fields
        existingCustomer.setCompanyName(customerMasterDTO.getCompanyName());
        existingCustomer.setCompanyEmail(customerMasterDTO.getCompanyEmail());
        existingCustomer.setContactPersonName(customerMasterDTO.getContactPersonName());
        existingCustomer.setCompanyContactNumber(customerMasterDTO.getCompanyContactNumber());
        existingCustomer.setCompanyAlternateContactNumber(customerMasterDTO.getCompanyAlternateContactNumber());
        existingCustomer.setCompanyAddress(customerMasterDTO.getCompanyAddress());



        // Save and return updated customer DTO
        customerMasterRepository.save(existingCustomer);
        return mapToDTO(existingCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        // Fetch the customer
        CustomerMaster customer = customerMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Fetch all bookings associated with the customer
        List<Booking> bookings = bookingRepository.findByCustomerMasterId(id);

        // Nullify the customer_master_id in all related bookings
        for (Booking booking : bookings) {
            booking.setCustomerMaster(null); // Nullify the reference
        }
        bookingRepository.saveAll(bookings); // Save the updated bookings

        // Delete the customer
        customerMasterRepository.delete(customer);
    }

    // Get customer by ID
    @Override
    public CustomerMasterDTO getCustomerById(Long id) {
        CustomerMaster customer = customerMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return mapToDTO(customer);
    }

    // Get all customers
    @Override
    public List<CustomerMasterDTO> getAllCustomers() {
        List<CustomerMaster> customers = customerMasterRepository.findAll();
        return customers.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Mapper methods

    // Convert entity to DTO
    private CustomerMasterDTO mapToDTO(CustomerMaster customer) {
        CustomerMasterDTO dto = new CustomerMasterDTO();
        dto.setId(customer.getId());
        dto.setCompanyName(customer.getCompanyName());
        dto.setCompanyEmail(customer.getCompanyEmail());
        dto.setContactPersonName(customer.getContactPersonName());
        dto.setCompanyContactNumber(customer.getCompanyContactNumber());
        dto.setCompanyAlternateContactNumber(customer.getCompanyAlternateContactNumber());
        dto.setCompanyAddress(customer.getCompanyAddress());
        dto.setGstNumber(customer.getGstNumber());
        return dto;
    }

    // Convert DTO to entity
    private CustomerMaster mapToEntity(CustomerMasterDTO dto) {
        CustomerMaster customer = new CustomerMaster();
        customer.setId(dto.getId());
        customer.setCompanyName(dto.getCompanyName());
        customer.setCompanyEmail(dto.getCompanyEmail());
        customer.setContactPersonName(dto.getContactPersonName());
        customer.setCompanyContactNumber(dto.getCompanyContactNumber());
        customer.setCompanyAlternateContactNumber(dto.getCompanyAlternateContactNumber());
        customer.setCompanyAddress(dto.getCompanyAddress());
        customer.setGstNumber(dto.getGstNumber());  // Set GST if provided
        return customer;
    }
}
