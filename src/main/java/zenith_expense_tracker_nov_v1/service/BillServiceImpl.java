package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.BillDTO;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.*;
import zenith_expense_tracker_nov_v1.exception.BillAlreadyGeneratedException;
import zenith_expense_tracker_nov_v1.exception.BillFetchException;
import zenith_expense_tracker_nov_v1.exception.BookingNotFoundException;
import zenith_expense_tracker_nov_v1.repository.BillRepository;
import zenith_expense_tracker_nov_v1.repository.BookingRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;
import zenith_expense_tracker_nov_v1.service.BillService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRevenueService propertyRevenueService;

    @Override
    public BillDTO generateBill(BillGenerationRequest request, Long userId) {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Validate booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + request.getBookingId()));

        // Check if a bill already exists
        if (billRepository.existsByBookingId(request.getBookingId())) {
            throw new BillAlreadyGeneratedException("Bill already exists for booking ID: " + request.getBookingId());
        }

        // Validate access and amounts
        if (!validateUserAccess(userId, booking.getId())) {
            throw new SecurityException("Unauthorized: User does not have access to generate bill for this booking");
        }
        validateAmounts(request);

        // Calculate totalAmount
        Double totalAmount = request.getAmount() + request.getCgst() + request.getSgst();

        // Create and save the bill
        Bill bill = new Bill();
        bill.setInvoiceNumber(generateInvoiceNumber());
        bill.setBooking(booking);
        bill.setAmount(request.getAmount());
        bill.setTotalAmount(totalAmount);
        bill.setCgst(request.getCgst());
        bill.setSgst(request.getSgst());
        bill.setBillDateTime(LocalDateTime.now());
        bill.setGeneratedDate(LocalDateTime.now());
        bill.setGeneratedByUser(user);
        bill.setPropertyName(booking.getProperty().getName());
        bill.setModeOfPayment(request.getModeOfPayment());

        booking.setCheckedOut(true);
        bookingRepository.save(booking);


        // Save the bill
        Bill savedBill = billRepository.save(bill);

        // Create property revenue entry from bill
        propertyRevenueService.createRevenueFromBill(savedBill);

        return convertToDTO(savedBill);
    }

    @Override
    public BillDTO getBillByBookingId(Long bookingId) {
        // Check if booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for ID: " + bookingId));

        try {
            Bill bill = billRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new BillFetchException("No bill found for booking ID: " + bookingId));

            return convertToDTO(bill);
        } catch (Exception e) {
            throw new BillFetchException("Error occurred while fetching bill for booking ID: " + bookingId);
        }
    }
    @Override
    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found with id: " + id));
        return convertToDTO(bill);
    }

    @Override
    public List<BillDTO> getBillsByUserId(Long userId) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<Bill> bills;
        if (user.getAccountType() == AccountType.ADMIN) {
            // Admin can see all bills
            bills = billRepository.findAll();
        } else {
            // Regular users can only see bills for their property
            bills = billRepository.findByGeneratedByUserId(userId);
        }

        return bills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateUserAccess(Long userId, Long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // Admin has access to all bookings
        if (user.getAccountType() == AccountType.ADMIN) {
            return true;
        }

        // Regular users can only access bookings for their assigned property
        return user.getProperty() != null &&
                booking.getProperty() != null &&
                user.getProperty().getId().equals(booking.getProperty().getId());
    }

    private void validateAmounts(BillGenerationRequest request) {
        if (request.getTotalAmount() == null || request.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        if (request.getCgst() == null || request.getCgst() < 0) {
            throw new IllegalArgumentException("CGST cannot be negative");
        }
        if (request.getSgst() == null || request.getSgst() < 0) {
            throw new IllegalArgumentException("SGST cannot be negative");
        }
    }

    private String generateInvoiceNumber() {
        String prefix = "INV";
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        String invoiceNumber = prefix + timestamp + random;

        // Ensure uniqueness
        while (billRepository.existsByInvoiceNumber(invoiceNumber)) {
            random = String.format("%04d", new Random().nextInt(10000));
            invoiceNumber = prefix + timestamp + random;
        }

        return invoiceNumber;
    }

    @Override
    public BillDTO updateBill(Long bookingId, BillUpdateRequest updateRequest, Long userId) {
        if (!validateUserAccess(userId, bookingId)) {
            throw new SecurityException("Unauthorized: User does not have access to update this bill");
        }

        Bill bill = billRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No bill found for booking ID: " + bookingId));

        validateUpdateAmounts(updateRequest);

        bill.setAmount(updateRequest.getAmount());
        bill.setTotalAmount(updateRequest.getAmount() + updateRequest.getCgst() + updateRequest.getSgst());
        bill.setCgst(updateRequest.getCgst());
        bill.setSgst(updateRequest.getSgst());

        Bill updatedBill = billRepository.save(bill);
        return convertToDTO(updatedBill);
    }

    private void validateUpdateAmounts(BillUpdateRequest updateRequest) {
        if (updateRequest.getAmount() == null || updateRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (updateRequest.getCgst() == null || updateRequest.getCgst() < 0) {
            throw new IllegalArgumentException("CGST cannot be negative.");
        }
        if (updateRequest.getSgst() == null || updateRequest.getSgst() < 0) {
            throw new IllegalArgumentException("SGST cannot be negative.");
        }

        // Validate total amount consistency
        double calculatedTotal = updateRequest.getAmount() + updateRequest.getCgst() + updateRequest.getSgst();
        if (Double.compare(calculatedTotal, updateRequest.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Total amount does not match the sum of amount, CGST, and SGST.");
        }
    }

    @Override
    public List<BillDTO> getAllBillsWithUserInfo() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(bill -> {
                    BillDTO dto = new BillDTO();
                    BeanUtils.copyProperties(bill, dto);

                    // Set basic bill information
                    dto.setId(bill.getId());
                    dto.setInvoiceNumber(bill.getInvoiceNumber());
                    dto.setTotalAmount(bill.getTotalAmount());
                    dto.setCgst(bill.getCgst());
                    dto.setSgst(bill.getSgst());
                    dto.setBillDateTime(bill.getBillDateTime());
                    dto.setGeneratedDate(bill.getGeneratedDate());
                    dto.setPropertyName(bill.getPropertyName());

                    // Set booking information
                    Booking booking = bill.getBooking();
                    if (booking != null) {
                        dto.setBookingId(booking.getId());

                        // Create booking details
                        BookingDTO bookingDTO = new BookingDTO();
                        BeanUtils.copyProperties(booking, bookingDTO);
                        bookingDTO.setPropertyId(booking.getProperty().getId());
                        bookingDTO.setUserId(booking.getUser().getId());
                        dto.setBookingDetails(bookingDTO);
                    }

                    // Set user information
                    User generatedByUser = bill.getGeneratedByUser();
                    if (generatedByUser != null) {
                        dto.setGeneratedByUserId(generatedByUser.getId());
                        dto.setGeneratedByUsername(generatedByUser.getName());
                        dto.setGeneratedByUserEmail(generatedByUser.getEmail());
                        dto.setGeneratedByUserType(generatedByUser.getAccountType());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        BeanUtils.copyProperties(bill, dto);

        // Set IDs and basic info
        dto.setBookingId(bill.getBooking().getId());
        dto.setGeneratedByUserId(bill.getGeneratedByUser().getId());
        dto.setGeneratedByUsername(bill.getGeneratedByUser().getName());

        // Convert booking details
        BookingDTO bookingDTO = new BookingDTO();
        Booking booking = bill.getBooking();
        BeanUtils.copyProperties(booking, bookingDTO);

        // Set booking-related properties
        bookingDTO.setPropertyId(booking.getProperty().getId());
        bookingDTO.setUserId(booking.getUser().getId());
        bookingDTO.setCreatedByUsername(booking.getCreatedByUsername());

        // Add booking details to bill DTO
        dto.setBookingDetails(bookingDTO);

        return dto;
    }
}
