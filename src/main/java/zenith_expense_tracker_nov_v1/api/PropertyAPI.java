package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.PropertyDTO;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.service.PropertyService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/properties")
public class PropertyAPI {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        return new ResponseEntity<>(propertyService.createProperty(property), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property propertyDetails) {
        return new ResponseEntity<>(propertyService.updateProperty(id, propertyDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public List<PropertyDTO> getAllProperties() {
        return propertyService.getAllProperties();
    }
    @GetMapping("/{propertyId}/users")
    public ResponseEntity<List<UserDTO>> getUsersAssignedToProperty(@PathVariable Long propertyId) {
        List<UserDTO> users = propertyService.getUsersAssignedToProperty(propertyId);
        return ResponseEntity.ok(users);
    }
}
