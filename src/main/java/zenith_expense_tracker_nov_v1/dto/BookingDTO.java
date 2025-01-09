package zenith_expense_tracker_nov_v1.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {

    private Long id;
    private String guestName;
    private String email;
    private String phoneNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomNo;
    private int noOfGuests;
    private int noOfChildren;
    private String companyName;
    private LocalDateTime bookingDateTime;
    private boolean isCheckedOut;
    private String documentSubmitted;
    private Long propertyId;
    private Long userId;
    private String createdByUsername;

    public BookingDTO() {
    }

    public BookingDTO(Long id, String guestName, String email, String phoneNo, LocalDate checkInDate, LocalDate checkOutDate,
                      String roomNo, int noOfGuests, int noOfChildren, String companyName, LocalDateTime bookingDateTime,
                      boolean isCheckedOut, String documentSubmitted, Long propertyId, Long userId, String createdByUsername) {
        this.id = id;
        this.guestName = guestName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomNo = roomNo;
        this.noOfGuests = noOfGuests;
        this.noOfChildren = noOfChildren;
        this.companyName = companyName;
        this.bookingDateTime = bookingDateTime;
        this.isCheckedOut = isCheckedOut;
        this.documentSubmitted = documentSubmitted;
        this.propertyId = propertyId;
        this.userId = userId;
        this.createdByUsername = createdByUsername;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(int noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    public int getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(int noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }

    public String getDocumentSubmitted() {
        return documentSubmitted;
    }

    public void setDocumentSubmitted(String documentSubmitted) {
        this.documentSubmitted = documentSubmitted;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}