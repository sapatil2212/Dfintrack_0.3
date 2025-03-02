package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.PropertyRevenueDTO;

import java.util.List;

public interface PropertyRevenueService {
    PropertyRevenueDTO createRevenue(PropertyRevenueDTO dto, Long userId);
    PropertyRevenueDTO getRevenueById(Long id, Long userId);

    List<PropertyRevenueDTO> getAllRevenues(Long userId);

    PropertyRevenueDTO updateRevenue(Long id, PropertyRevenueDTO dto, Long userId);
    void deleteRevenue(Long id, Long userId);

}
