package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.PropertyDTO;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        property.setName(propertyDetails.getName());
        property.setDescription(propertyDetails.getDescription());
        property.setAddress(propertyDetails.getAddress());

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property not found with ID: " + id);
        }
        propertyRepository.deleteById(id);
    }
    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();

        // Convert Property entities to PropertyDTOs and include user information
        return properties.stream().map(property -> {
            Long userId = property.getUser() != null ? property.getUser().getId() : null;
            String userName = property.getUser() != null ? property.getUser().getName() : "N/A";
            return new PropertyDTO(
                    property.getId(),
                    property.getName(),
                    property.getAddress(),
                    property.getDescription(),
                    userId,
                    userName
            );
        }).collect(Collectors.toList());
    }
    public List<UserDTO> getUsersAssignedToProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));

        // Fetch users who are assigned to this property
        List<User> users = userRepository.findByProperty(property);

        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getAccountType(),
                        user.getRegistrationDate(),
                        user.getSecurityKey(),
                        user.getProperty() != null ? user.getProperty().getId() : null,
                        user.getProperty() != null ? user.getProperty().getName() : null
                ))
                .collect(Collectors.toList());
    }
    }

