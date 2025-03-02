package zenith_expense_tracker_nov_v1.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.PropertyDTO;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.service.PropertyService;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/properties")
public class PropertyAPI {

    @Autowired
    private PropertyService propertyService;

    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(PropertyAPI.class);

    // Create a new property using PropertyDTO
    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyDTO propertyDTO) {
        try {
            // Convert PropertyDTO to Property entity
            Property property = new Property();
            property.setName(propertyDTO.getName());
            property.setAddress(propertyDTO.getAddress());
            property.setDescription(propertyDTO.getDescription());

            // Create property and return the PropertyDTO
            Property createdProperty = propertyService.createProperty(property);
            PropertyDTO responseDTO = new PropertyDTO(
                    createdProperty.getId(),
                    createdProperty.getName(),
                    createdProperty.getAddress(),
                    createdProperty.getDescription()
            );
            logger.info("Created property with ID: {}", createdProperty.getId()); // Log the creation
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating property: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid property data: " + e.getMessage());
        }
    }

    // Update an existing property using PropertyDTO
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO propertyDTO) {
        try {
            // Convert PropertyDTO to Property entity
            Property propertyDetails = new Property();
            propertyDetails.setName(propertyDTO.getName());
            propertyDetails.setAddress(propertyDTO.getAddress());
            propertyDetails.setDescription(propertyDTO.getDescription());

            // Update the property
            Property updatedProperty = propertyService.updateProperty(id, propertyDetails);
            PropertyDTO responseDTO = new PropertyDTO(
                    updatedProperty.getId(),
                    updatedProperty.getName(),
                    updatedProperty.getAddress(),
                    updatedProperty.getDescription()
            );
            logger.info("Updated property with ID: {}", updatedProperty.getId()); // Log the update
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Property not found with ID: {}", id);
            throw e; // This exception is already handled globally
        }
    }

    // Delete a property by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.deleteProperty(id);
            logger.info("Deleted property with ID: {}", id); // Log the deletion
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            logger.error("Property not found with ID: {}", id);
            throw e; // This exception is already handled globally
        }
    }

    // Get all properties and return a list of PropertyDTO
    @GetMapping
    public List<PropertyDTO> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        logger.info("Retrieved all properties"); // Log the retrieval of properties
        return properties;
    }
}
