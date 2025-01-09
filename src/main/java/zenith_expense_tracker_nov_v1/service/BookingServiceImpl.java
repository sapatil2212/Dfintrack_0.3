package zenith_expense_tracker_nov_v1.service;



import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.BookingDTO;
import zenith_expense_tracker_nov_v1.entity.Booking;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.repository.BookingRepository;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDto) {
        validateBookingDto(bookingDto);
        Booking booking = convertToEntity(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        return convertToDto(savedBooking);
    }

    private void validateBookingDto(BookingDTO bookingDto) {
        if (bookingDto == null) {
            throw new IllegalArgumentException("Booking data cannot be null");
        }
        if (bookingDto.getPropertyId() == null) {
            throw new IllegalArgumentException("Property ID cannot be null");
        }
        if (bookingDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (bookingDto.getGuestName() == null || bookingDto.getGuestName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        if (bookingDto.getCheckInDate() == null) {
            throw new IllegalArgumentException("Check-in date cannot be null");
        }
        if (bookingDto.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-out date cannot be null");
        }
    }


    @Override
    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDto) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        BeanUtils.copyProperties(bookingDto, existingBooking, "id", "property", "user");

        if (bookingDto.getPropertyId() != null) {
            Property property = propertyRepository.findById(bookingDto.getPropertyId())
                    .orElseThrow(() -> new RuntimeException("Property not found"));
            existingBooking.setProperty(property);
        }

        if (bookingDto.getUserId() != null) {
            User user = userRepository.findById(bookingDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingBooking.setUser(user);
        }

        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToDto(updatedBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }


    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return convertToDto(booking);
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDTO checkoutBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setCheckedOut(true);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDto(updatedBooking);
    }

    @Override
    @Transactional
    public BookingDTO updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setCheckedOut("CHECKEDOUT".equalsIgnoreCase(status));
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDto(updatedBooking);
    }

    private BookingDTO convertToDto(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking entity cannot be null");
        }

        BookingDTO bookingDto = new BookingDTO();
        BeanUtils.copyProperties(booking, bookingDto);

        if (booking.getProperty() != null) {
            bookingDto.setPropertyId(booking.getProperty().getId());
        }

        if (booking.getUser() != null) {
            bookingDto.setUserId(booking.getUser().getId());
            bookingDto.setCreatedByUsername(booking.getUser().getName());
        }

        return bookingDto;
    }



    private Booking convertToEntity(BookingDTO bookingDto) {
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);

        Property property = propertyRepository.findById(bookingDto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));
        booking.setProperty(property);

        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        booking.setUser(user);
        BeanUtils.copyProperties(bookingDto, booking, "property", "user");
        booking.setProperty(property);
        booking.setUser(user);

        return booking;
    }


}
