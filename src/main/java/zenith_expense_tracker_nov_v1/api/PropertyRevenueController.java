package zenith_expense_tracker_nov_v1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.PropertyRevenueDTO;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;

import zenith_expense_tracker_nov_v1.exception.UnauthorizedException;
import zenith_expense_tracker_nov_v1.service.PropertyRevenueService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/property-revenues")
@RequiredArgsConstructor
public class PropertyRevenueController {
    private final PropertyRevenueService propertyRevenueService;

    @PostMapping("/create")
    public ResponseEntity<?> createRevenue(
            @RequestBody PropertyRevenueDTO dto,
            @RequestParam Long userId) {
        try {
            PropertyRevenueDTO createdRevenue = propertyRevenueService.createRevenue(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Revenue created successfully", createdRevenue));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while creating revenue."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRevenue(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            PropertyRevenueDTO revenue = propertyRevenueService.getRevenueById(id, userId);
            return ResponseEntity.ok(new ApiResponse<>("Revenue retrieved successfully", revenue));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while retrieving the revenue."));
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getRevenuesByProperty(
            @PathVariable Long propertyId,
            @RequestParam Long userId) {
        try {
            List<PropertyRevenueDTO> revenues = propertyRevenueService.getAllRevenuesByPropertyId(propertyId, userId);
            return ResponseEntity.ok(new ApiResponse<>("Revenues retrieved successfully", revenues));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while retrieving revenues."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRevenue(
            @PathVariable Long id,
            @RequestBody PropertyRevenueDTO dto,
            @RequestParam Long userId) {
        try {
            PropertyRevenueDTO updatedRevenue = propertyRevenueService.updateRevenue(id, dto, userId);
            return ResponseEntity.ok(new ApiResponse<>("Revenue updated successfully", updatedRevenue));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while updating the revenue."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRevenue(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            propertyRevenueService.deleteRevenue(id, userId);
            return ResponseEntity.ok(new ApiResponse<>("Revenue deleted successfully"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while deleting the revenue."));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRevenues(
            @RequestParam Long userId) {
        try {
            List<PropertyRevenueDTO> revenues = propertyRevenueService.getAllRevenues(userId);
            return ResponseEntity.ok(new ApiResponse<>("All revenues retrieved successfully", revenues));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while retrieving all revenues."));
        }
    }

    @GetMapping("/property-wise")
    public ResponseEntity<?> getRevenuesPropertyWise(
            @RequestParam Long userId) {
        try {
            Map<String, List<PropertyRevenueDTO>> revenuesGrouped = propertyRevenueService.getRevenuesGroupedByProperty(userId);
            return ResponseEntity.ok(new ApiResponse<>("Revenues grouped by property retrieved successfully", revenuesGrouped));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("An error occurred while retrieving property-wise revenues."));
        }
    }

    // ApiResponse class to standardize responses
    public static class ApiResponse<T> {
        private String message;
        private T data;

        public ApiResponse(String message) {
            this.message = message;
        }

        public ApiResponse(String message, T data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}
