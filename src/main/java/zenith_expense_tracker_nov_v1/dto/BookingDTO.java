package zenith_expense_tracker_nov_v1.dto;

import lombok.*;
import zenith_expense_tracker_nov_v1.enums.BillingType;
import zenith_expense_tracker_nov_v1.enums.BookingStatus;
import zenith_expense_tracker_nov_v1.enums.BookingType;
import zenith_expense_tracker_nov_v1.enums.OccupancyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDTO {

    private Long id;
    private String bookingNumber;
    private Long propertyId;

    private BookingType bookingType;
    private Long customerMasterId;
    private String customerCompanyName;
    private String customerCompanyEmail;
    private String customerContactPersonName;
    private String customerCompanyContactNumber;
    private String customerCompanyAddress;
    private String customerGstNumber;


    private String guestName;
    private String phoneNumber;
    private int numberOfRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private OccupancyType occupancyType;
    private String idProof;
    private BookingStatus bookingStatus;
    private BigDecimal bookingAmount;
    private BigDecimal advanceAmount;
    private BillingType billingType;
    private Long bankAccountId;
    private LocalDateTime bookingDateTime;
    private String email;
    private String paymentMode;
    private BigDecimal refundAmount;
    private Boolean acceptFoodGST;

    // Add getter and setter
    public Boolean getAcceptFoodGST() {
        return acceptFoodGST;
    }

    public void setAcceptFoodGST(Boolean acceptFoodGST) {
        this.acceptFoodGST = acceptFoodGST;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getCustomerMasterId() {
        return customerMasterId;
    }

    public void setCustomerMasterId(Long customerMasterId) {
        this.customerMasterId = customerMasterId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public OccupancyType getOccupancyType() {
        return occupancyType;
    }

    public void setOccupancyType(OccupancyType occupancyType) {
        this.occupancyType = occupancyType;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public BigDecimal getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(BigDecimal bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}