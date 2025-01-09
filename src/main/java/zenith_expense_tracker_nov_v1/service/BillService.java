package zenith_expense_tracker_nov_v1.service;


import zenith_expense_tracker_nov_v1.dto.BillDTO;

import zenith_expense_tracker_nov_v1.entity.BillGenerationRequest;
import zenith_expense_tracker_nov_v1.entity.BillUpdateRequest;

import java.util.List;
import java.util.Optional;


public interface BillService {
    BillDTO generateBill(BillGenerationRequest request, Long userId);
    BillDTO getBillById(Long id);
    List<BillDTO> getBillsByUserId(Long userId);
    boolean validateUserAccess(Long userId, Long bookingId);
    List<BillDTO> getAllBillsWithUserInfo();
    BillDTO updateBill(Long bookingId, BillUpdateRequest updateRequest, Long userId);
    BillDTO getBillByBookingId(Long bookingId);
}