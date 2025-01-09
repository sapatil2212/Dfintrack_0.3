package zenith_expense_tracker_nov_v1.service;

import zenith_expense_tracker_nov_v1.dto.PersonalRevenueDTO;
import java.util.List;

public interface PersonalRevenueService {
    PersonalRevenueDTO createRevenue(PersonalRevenueDTO dto, Long adminId);
    PersonalRevenueDTO getRevenueById(Long id, Long adminId);
    List<PersonalRevenueDTO> getAllRevenuesByAdminId(Long adminId);
    PersonalRevenueDTO updateRevenue(Long id, PersonalRevenueDTO dto, Long adminId);
    void deleteRevenue(Long id, Long adminId);
}