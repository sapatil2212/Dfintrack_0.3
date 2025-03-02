package zenith_expense_tracker_nov_v1.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.BankAccountTransactionsDTO;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.BankAccount;
import zenith_expense_tracker_nov_v1.entity.Booking;
import zenith_expense_tracker_nov_v1.entity.CustomerMaster;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.enums.BookingType;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.repository.*;

import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CustomerMasterRepository customerMasterRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private BankAccountTransactionsService bankAccountTransactionsService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    // Method to generate booking number
    private String generateBookingNumber() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM"); // Month abbreviation (e.g., FEB)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd"); // Day of the month (e.g., 26)
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy"); // Year (e.g., 2025)

        String month = now.format(monthFormatter).toUpperCase(); // Month in uppercase
        String date = now.format(dateFormatter); // Day of the month
        String year = now.format(yearFormatter); // Year

        // Fetch the latest serial number from the database
        Long latestSerialNumber = bookingRepository.count() + 1; // Increment by 1 for the new booking
        String serialNumber = String.format("%02d", latestSerialNumber); // Pad with leading zeros (e.g., 01)

        // Format: ZBKFEB26202501
        return "ZBK" + month + date + year + serialNumber;
    }

    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {
        // Generate booking number
        String bookingNumber = generateBookingNumber();

        // Convert DTO to Entity
        Booking booking = new Booking();
        booking.setBookingNumber(bookingNumber);
        booking.setBookingDateTime(LocalDateTime.now());
        booking.setBookingType(bookingDTO.getBookingType());
        booking.setProperty(propertyRepository.findById(bookingDTO.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found")));

        // Handle corporate bookings
        if (bookingDTO.getBookingType() == BookingType.CORPORATE) {
            CustomerMaster customer = customerMasterRepository.findById(bookingDTO.getCustomerMasterId())
                    .orElseThrow(() -> new RuntimeException("Customer Master not found"));

            booking.setCustomerCompanyName(customer.getCompanyName());
            booking.setCustomerCompanyEmail(customer.getCompanyEmail());
            booking.setCustomerContactPersonName(customer.getContactPersonName());
            booking.setCustomerCompanyContactNumber(customer.getCompanyContactNumber());
            booking.setCustomerCompanyAddress(customer.getCompanyAddress());
            booking.setCustomerGstNumber(customer.getGstNumber());
            booking.setCustomerMaster(customer);
        }

        // Set other fields
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setPhoneNumber(bookingDTO.getPhoneNumber());
        booking.setNumberOfRooms(bookingDTO.getNumberOfRooms());
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setOccupancyType(bookingDTO.getOccupancyType());
        booking.setIdProof(bookingDTO.getIdProof());
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setBookingAmount(bookingDTO.getBookingAmount());
        booking.setAdvanceAmount(bookingDTO.getAdvanceAmount());
        booking.setBillingType(bookingDTO.getBillingType());
        booking.setPaymentMode(bookingDTO.getPaymentMode());
        booking.setEmail(bookingDTO.getEmail());

        // Handle bank account
        BankAccount bankAccount = bankAccountRepository.findById(bookingDTO.getBankAccountId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));
        booking.setBankAccount(bankAccount);

        // Calculate remaining amount
        booking.setRemainingAmount(booking.getBookingAmount().subtract(booking.getAdvanceAmount()));

        // Calculate GST amounts
        if (booking.getBillingType() == BillingType.GST) {
            BigDecimal stayGST = booking.getBookingAmount().multiply(new BigDecimal("0.12"));
            booking.setStayGSTAmount(stayGST);

            BigDecimal foodGST = BigDecimal.ZERO;
            if (Boolean.TRUE.equals(bookingDTO.getAcceptFoodGST())) {
                foodGST = booking.getBookingAmount().multiply(new BigDecimal("0.05"));
            }
            booking.setFoodGSTAmount(foodGST);

            booking.setTotalBillAmount(booking.getBookingAmount().add(stayGST).add(foodGST));
        } else {
            booking.setStayGSTAmount(BigDecimal.ZERO);
            booking.setFoodGSTAmount(BigDecimal.ZERO);
            booking.setTotalBillAmount(booking.getBookingAmount());
        }

        // Update bank account balance and create a transaction
        if (booking.getBookingStatus() == BookingStatus.PROFORMA) {
            // Add advance amount to the bank account
            bankAccount.setBalance(bankAccount.getBalance().add(booking.getAdvanceAmount()));
            createTransaction(bankAccount, booking.getAdvanceAmount(), "CREDIT", "Advance payment for booking " + bookingNumber);
        } else if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            // Add the full totalBillAmount to the bank account
            bankAccount.setBalance(bankAccount.getBalance().add(booking.getTotalBillAmount()));
            createTransaction(bankAccount, booking.getTotalBillAmount(), "CREDIT", "Full payment for booking " + bookingNumber);
        }

        // Save the updated bank account
        bankAccountRepository.save(bankAccount);

        // Save the booking
        return bookingRepository.save(booking);
    }
    // Helper method to create a transaction
    private void createTransaction(BankAccount bankAccount, BigDecimal amount, String transactionType, String description) {
        BankAccountTransactionsDTO transactionDTO = new BankAccountTransactionsDTO();
        transactionDTO.setTransactionType(transactionType);
        transactionDTO.setAmount(amount);
        transactionDTO.setTransactionDate(LocalDateTime.now()); // Automatically set the current date and time
        transactionDTO.setDescription(description);
        transactionDTO.setBankAccountId(bankAccount.getId());

        bankAccountTransactionsService.createTransaction(transactionDTO);
    }

    @Transactional
    public void cancelBooking(Long bookingId, BigDecimal refundAmount) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        BankAccount bankAccount = booking.getBankAccount();
        if (refundAmount.compareTo(bankAccount.getBalance()) > 0) {
            throw new RuntimeException("Insufficient balance in the bank account for refund");
        }

        // Deduct the refund amount from the bank account balance
        bankAccount.setBalance(bankAccount.getBalance().subtract(refundAmount));
        bankAccountRepository.save(bankAccount);

        // Update the booking status to CANCELLED
        booking.setBookingStatus(BookingStatus.CANCELLED);

        // Save the refund amount in the booking entity (if needed)
        booking.setRefundAmount(refundAmount); // Assuming you have a refundAmount field in the Booking entity

        // Save the updated booking
        bookingRepository.save(booking);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId, BigDecimal remainingAmount) {
        try {
            // Fetch the booking
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            // Check if the booking is in PROFORMA status
            if (booking.getBookingStatus() != BookingStatus.PROFORMA) {
                throw new RuntimeException("Only PROFORMA bookings can be confirmed");
            }

            // Validate that the remaining amount is not negative
            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Remaining amount cannot be negative.");
            }

            // Validate that the remaining amount does not exceed the total booking amount
            BigDecimal totalBookingAmount = booking.getBookingAmount();
            if (remainingAmount.compareTo(totalBookingAmount) > 0) {
                throw new IllegalArgumentException("Remaining amount cannot exceed the total booking amount.");
            }


            booking.setBookingStatus(BookingStatus.CONFIRMED);

            BigDecimal advanceAmount = totalBookingAmount.subtract(remainingAmount);
            booking.setAdvanceAmount(advanceAmount);
            booking.setRemainingAmount(BigDecimal.ZERO);


            booking.setTotalBillAmount(totalBookingAmount);


            BankAccount bankAccount = booking.getBankAccount();
            bankAccount.setBalance(bankAccount.getBalance().add(remainingAmount));
            bankAccountRepository.save(bankAccount);


            createTransaction(bankAccount, remainingAmount, "CREDIT", "Remaining payment for booking " + booking.getBookingNumber());

            return bookingRepository.save(booking);
        } catch (Exception e) {

            throw e;
        }
    }
    @Transactional
    public void deleteBooking(Long id) {
        // Check if the booking exists
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        // Refund the advance amount to the bank account (if applicable)
        if (booking.getBookingStatus() == BookingStatus.PROFORMA || booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            BankAccount bankAccount = booking.getBankAccount();
            bankAccount.setBalance(bankAccount.getBalance().subtract(booking.getAdvanceAmount()));
            bankAccountRepository.save(bankAccount);
        }

        // Delete the booking
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBookings() {
        // Refund all advance amounts to respective bank accounts
        List<Booking> bookings = bookingRepository.findAll();
        for (Booking booking : bookings) {
            if (booking.getBookingStatus() == BookingStatus.PROFORMA || booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                BankAccount bankAccount = booking.getBankAccount();
                bankAccount.setBalance(bankAccount.getBalance().subtract(booking.getAdvanceAmount()));
                bankAccountRepository.save(bankAccount);
            }
        }

        // Delete all bookings
        bookingRepository.deleteAll();
    }

    @Transactional
    public Booking updateBooking(Long id, BookingDTO bookingDTO) {
        // Fetch the existing booking
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        // Store old values for comparison
        BigDecimal oldTotalAmount = booking.getTotalBillAmount();
        BillingType oldBillingType = booking.getBillingType();
        BookingStatus oldBookingStatus = booking.getBookingStatus();

        // Validate that the bank account is not being updated
        if (bookingDTO.getBankAccountId() != null &&
                !bookingDTO.getBankAccountId().equals(booking.getBankAccount().getId())) {
            throw new RuntimeException("Updating the bank account is not allowed");
        }

        // Update the booking details (except bank account)
        booking.setBookingType(bookingDTO.getBookingType());
        booking.setProperty(propertyRepository.findById(bookingDTO.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found")));

        // Handle change from Personal to Corporate
        if (bookingDTO.getBookingType() == BookingType.CORPORATE) {
            // Fetch the customer master details
            CustomerMaster customer = customerMasterRepository.findById(bookingDTO.getCustomerMasterId())
                    .orElseThrow(() -> new RuntimeException("Customer Master not found"));

            // Update customer master information in the booking
            booking.setCustomerMaster(customer);
            booking.setCustomerCompanyName(customer.getCompanyName());
            booking.setCustomerCompanyEmail(customer.getCompanyEmail());
            booking.setCustomerContactPersonName(customer.getContactPersonName());
            booking.setCustomerCompanyContactNumber(customer.getCompanyContactNumber());
            booking.setCustomerCompanyAddress(customer.getCompanyAddress());
            booking.setCustomerGstNumber(customer.getGstNumber());
        } else {
            // If booking type is changed to Personal, clear customer master information
            booking.setCustomerMaster(null);
            booking.setCustomerCompanyName(null);
            booking.setCustomerCompanyEmail(null);
            booking.setCustomerContactPersonName(null);
            booking.setCustomerCompanyContactNumber(null);
            booking.setCustomerCompanyAddress(null);
            booking.setCustomerGstNumber(null);
        }

        // Update other fields
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setPhoneNumber(bookingDTO.getPhoneNumber());
        booking.setNumberOfRooms(bookingDTO.getNumberOfRooms());
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setOccupancyType(bookingDTO.getOccupancyType());
        booking.setIdProof(bookingDTO.getIdProof());
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setBookingAmount(bookingDTO.getBookingAmount());
        booking.setAdvanceAmount(bookingDTO.getAdvanceAmount());
        booking.setBillingType(bookingDTO.getBillingType());
        booking.setPaymentMode(bookingDTO.getPaymentMode());
        booking.setEmail(bookingDTO.getEmail());

        // Recalculate remaining amount
        booking.setRemainingAmount(booking.getBookingAmount().subtract(booking.getAdvanceAmount()));

        // Recalculate GST amounts
        if (booking.getBillingType() == BillingType.GST) {
            // Calculate stay GST (12% of booking amount)
            BigDecimal stayGST = booking.getBookingAmount().multiply(new BigDecimal("0.12"));
            booking.setStayGSTAmount(stayGST);

            // Calculate food GST (5% of booking amount, if applicable)
            BigDecimal foodGST = BigDecimal.ZERO;
            if (Boolean.TRUE.equals(bookingDTO.getAcceptFoodGST())) {
                foodGST = booking.getBookingAmount().multiply(new BigDecimal("0.05"));
            }
            booking.setFoodGSTAmount(foodGST);

            // Calculate total bill amount (booking amount + stay GST + food GST)
            booking.setTotalBillAmount(booking.getBookingAmount().add(stayGST).add(foodGST));
        } else {
            // For non-GST billing, GST amounts are zero
            booking.setStayGSTAmount(BigDecimal.ZERO);
            booking.setFoodGSTAmount(BigDecimal.ZERO);
            booking.setTotalBillAmount(booking.getBookingAmount());
        }

        // Handle bank account balance adjustments if there's a change in total amount
        if (!oldTotalAmount.equals(booking.getTotalBillAmount()) ||
                oldBillingType != booking.getBillingType() ||
                oldBookingStatus != booking.getBookingStatus()) {

            BankAccount bankAccount = booking.getBankAccount();

            // Reverse the old transaction
            if (oldBookingStatus == BookingStatus.CONFIRMED) {
                bankAccount.setBalance(bankAccount.getBalance().subtract(oldTotalAmount));
            } else if (oldBookingStatus == BookingStatus.PROFORMA) {
                bankAccount.setBalance(bankAccount.getBalance().subtract(booking.getAdvanceAmount()));
            }

            // Apply the new transaction
            if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                bankAccount.setBalance(bankAccount.getBalance().add(booking.getTotalBillAmount()));
            } else if (booking.getBookingStatus() == BookingStatus.PROFORMA) {
                bankAccount.setBalance(bankAccount.getBalance().add(booking.getAdvanceAmount()));
            }

            bankAccountRepository.save(bankAccount);
        }

        // Save the updated booking
        return bookingRepository.save(booking);
    }
}
