package zenith_expense_tracker_nov_v1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.PersonalRevenueDTO;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;

import zenith_expense_tracker_nov_v1.exception.UnauthorizedException;
import zenith_expense_tracker_nov_v1.service.PersonalRevenueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/personal-revenues")
@RequiredArgsConstructor
public class PersonalRevenueController {
    private final PersonalRevenueService personalRevenueService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRevenue(
            @RequestBody PersonalRevenueDTO dto,
            @RequestParam Long adminId) {
        PersonalRevenueDTO createdRevenue = personalRevenueService.createRevenue(dto, adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Revenue created successfully.");
        response.put("data", createdRevenue);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRevenue(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        PersonalRevenueDTO revenue = personalRevenueService.getRevenueById(id, adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Revenue retrieved successfully.");
        response.put("data", revenue);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllRevenues(@RequestParam Long adminId) {
        System.out.println("Received request to fetch revenues for adminId: " + adminId);
        List<PersonalRevenueDTO> revenues = personalRevenueService.getAllRevenuesByAdminId(adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All revenues retrieved successfully.");
        response.put("data", revenues);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRevenue(
            @PathVariable Long id,
            @RequestBody PersonalRevenueDTO dto,
            @RequestParam Long adminId) {
        PersonalRevenueDTO updatedRevenue = personalRevenueService.updateRevenue(id, dto, adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Revenue updated successfully.");
        response.put("data", updatedRevenue);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRevenue(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        personalRevenueService.deleteRevenue(id, adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Revenue deleted successfully.");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessException(UnauthorizedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "An unexpected error occurred. Please try again later.");
        errorResponse.put("details", ex.getMessage());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
