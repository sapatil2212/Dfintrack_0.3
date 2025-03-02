package zenith_expense_tracker_nov_v1.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.BillDTO;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.Bill;
import zenith_expense_tracker_nov_v1.entity.Booking;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.enums.BookingType;
import zenith_expense_tracker_nov_v1.exception.PaymentDueException;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.repository.BillRepository;
import zenith_expense_tracker_nov_v1.repository.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public Bill generateBill(Long bookingId) throws Exception {
        // Fetch the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Validate GST amounts are present
        if (booking.getFoodGSTAmount() == null || booking.getStayGSTAmount() == null) {
            throw new IllegalStateException("GST amounts cannot be null in booking with ID: " + bookingId);
        }

        // Fetch existing bill for the same booking and status (if any)
        Bill existingBill = billRepository.findByBookingIdAndBookingStatusEnum(bookingId, booking.getBookingStatus())
                .orElse(null);

        Bill bill;
        if (existingBill != null) {
            bill = existingBill;
            bill.setBillNumber(generateBillNumber(booking.getBookingStatus()));
            bill.setGuestName(booking.getGuestName());
            bill.setPhoneNumber(booking.getPhoneNumber());
            bill.setEmail(booking.getEmail());
            bill.setCheckInDate(booking.getCheckInDate().atStartOfDay());
            bill.setCheckOutDate(booking.getCheckOutDate().atStartOfDay());
            bill.setNumberOfRooms(booking.getNumberOfRooms());
            bill.setBookingAmount(booking.getBookingAmount());
            bill.setTotalAmount(booking.getTotalBillAmount());
            bill.setFoodGSTAmount(booking.getFoodGSTAmount());
            bill.setStayGSTAmount(booking.getStayGSTAmount());
            bill.setBillGenerationDateTime(LocalDateTime.now());
            bill.setPaymentMode(booking.getPaymentMode());
            bill.setBillingType(booking.getBillingType());
            bill.setOccupancyType(booking.getOccupancyType());
            bill.setBookingNumber(booking.getBookingNumber());
bill.setBookingDateTime(booking.getBookingDateTime());
        } else {
            // If no bill with the same status exists, create a new bill
            bill = Bill.builder()
                    .billNumber(generateBillNumber(booking.getBookingStatus()))
                    .bookingId(booking.getId())
                    .guestName(booking.getGuestName())
                    .phoneNumber(booking.getPhoneNumber())
                    .email(booking.getEmail())
                    .checkInDate(booking.getCheckInDate().atStartOfDay())
                    .checkOutDate(booking.getCheckOutDate().atStartOfDay())
                    .numberOfRooms(booking.getNumberOfRooms())
                    .bookingAmount(booking.getBookingAmount())
                    .totalAmount(booking.getTotalBillAmount())
                    .foodGSTAmount(booking.getFoodGSTAmount())
                    .stayGSTAmount(booking.getStayGSTAmount())
                    .billGenerationDateTime(LocalDateTime.now())
                    .paymentMode(booking.getPaymentMode())
                    .billingType(booking.getBillingType())
                    .occupancyType(booking.getOccupancyType())
                    .bookingStatusEnum(booking.getBookingStatus())
                    .bookingNumber(booking.getBookingNumber())
                    .bookingDateTime(booking.getBookingDateTime())
                    .build();
        }

        // Save or update the bill
        bill = billRepository.save(bill);

        // Generate PDF
        byte[] pdfContent = pdfService.generateBillPdf(bill);

        // Send email with PDF attached
        emailService.sendBillEmail(bill.getEmail(), bill, pdfContent);

        return bill;
    }

    private String generateBillNumber(BookingStatus bookingStatus) {
        LocalDateTime now = LocalDateTime.now();
        String statusPrefix = bookingStatus == BookingStatus.PROFORMA ? "PROFORMA" : "CONFIRMED";
        int randomSuffix = new Random().nextInt(1000);
        return String.format("%s-INVZEN%d%02d%02d%02d%03d",
                statusPrefix,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                randomSuffix);
    }

    public BillDTO getBillByBookingId(Long bookingId) throws ResourceNotFoundException {
        // Fetch the latest bill by bookingId, ordered by billGenerationDateTime in descending order
        Bill bill = billRepository.findTopByBookingIdOrderByBillGenerationDateTimeDesc(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found for booking ID: " + bookingId));

        // Map Bill entity to BillDTO
        BillDTO billDTO = modelMapper.map(bill, BillDTO.class);

        // Fetch the associated booking details
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        // Map Booking entity to BookingDTO
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        // Set the booking details in the BillDTO
        billDTO.setBooking(bookingDTO);

        return billDTO;
    }

    public void deleteBill(Long billId) throws ResourceNotFoundException {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));
        billRepository.delete(bill);
    }
}
