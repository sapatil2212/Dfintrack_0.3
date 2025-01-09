package zenith_expense_tracker_nov_v1.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNo;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private String roomNo;

    @Column(nullable = false)
    private int noOfGuests;

    @Column(nullable = false)
    private int noOfChildren;

    private String companyName;

    @Column(nullable = false)
    private LocalDateTime bookingDateTime;

    @Column(nullable = false)
    private boolean isCheckedOut;

    private String documentSubmitted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_by_username")
    private String createdByUsername;

    // Constructors, getters, and setters

    public Booking() {
    }

    public Booking(String guestName, String email, String phoneNo, LocalDate checkInDate, LocalDate checkOutDate,
                   String roomNo, int noOfGuests, int noOfChildren, String companyName, LocalDateTime bookingDateTime,
                   boolean isCheckedOut, String documentSubmitted, Property property, User user, String createdByUsername) {
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
        this.property = property;
        this.user = user;
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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

}
