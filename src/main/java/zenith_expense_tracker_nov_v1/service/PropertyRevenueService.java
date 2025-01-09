package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.PropertyRevenueDTO;
import zenith_expense_tracker_nov_v1.entity.Bill;

import java.util.List;
import java.util.Map;

public interface PropertyRevenueService {
    PropertyRevenueDTO createRevenue(PropertyRevenueDTO dto, Long userId);
    PropertyRevenueDTO getRevenueById(Long id, Long userId);
    List<PropertyRevenueDTO> getAllRevenuesByPropertyId(Long propertyId, Long userId);
    List<PropertyRevenueDTO> getAllRevenuesByUserId(Long userId);
    List<PropertyRevenueDTO> getAllRevenues(Long userId);
    Map<String, List<PropertyRevenueDTO>> getRevenuesGroupedByProperty(Long userId);
    PropertyRevenueDTO updateRevenue(Long id, PropertyRevenueDTO dto, Long userId);
    void deleteRevenue(Long id, Long userId);
    void createRevenueFromBill(Bill bill);
}