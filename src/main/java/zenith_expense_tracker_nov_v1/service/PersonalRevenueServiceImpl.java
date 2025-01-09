package zenith_expense_tracker_nov_v1.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.PersonalRevenueDTO;
import zenith_expense_tracker_nov_v1.entity.PersonalRevenue;
import zenith_expense_tracker_nov_v1.entity.User;

import zenith_expense_tracker_nov_v1.repository.PersonalRevenueRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;
import zenith_expense_tracker_nov_v1.service.PersonalRevenueService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalRevenueServiceImpl implements PersonalRevenueService {

    private final PersonalRevenueRepository personalRevenueRepository;
    private final UserRepository userRepository;

    @Override
    public PersonalRevenueDTO createRevenue(PersonalRevenueDTO dto, Long adminId) {
        User admin = validateAdmin(adminId);

        PersonalRevenue revenue = new PersonalRevenue();
        BeanUtils.copyProperties(dto, revenue);
        revenue.setDateTime(LocalDateTime.now());
        revenue.setAdmin(admin);

        PersonalRevenue savedRevenue = personalRevenueRepository.save(revenue);
        return convertToDTO(savedRevenue);
    }

    @Override
    public PersonalRevenueDTO getRevenueById(Long id, Long adminId) {
        User admin = validateAdmin(adminId);

        PersonalRevenue revenue = personalRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        // Validate that this revenue belongs to the admin
        if (!revenue.getAdmin().getId().equals(admin.getId())) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        return convertToDTO(revenue);
    }

    @Override
    public List<PersonalRevenueDTO> getAllRevenuesByAdminId(Long adminId) {
        User admin = validateAdmin(adminId);

        List<PersonalRevenue> revenues = personalRevenueRepository.findByAdminId(adminId);
        return revenues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonalRevenueDTO updateRevenue(Long id, PersonalRevenueDTO dto, Long adminId) {
        User admin = validateAdmin(adminId);

        PersonalRevenue revenue = personalRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        // Validate that this revenue belongs to the admin
        if (!revenue.getAdmin().getId().equals(admin.getId())) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        // Update fields
        revenue.setAmount(dto.getAmount());
        revenue.setRevenueType(dto.getRevenueType());
        revenue.setBank(dto.getBank());
        revenue.setPaymentMode(dto.getPaymentMode());
        revenue.setDescription(dto.getDescription());

        PersonalRevenue updatedRevenue = personalRevenueRepository.save(revenue);
        return convertToDTO(updatedRevenue);
    }

    @Override
    public void deleteRevenue(Long id, Long adminId) {
        User admin = validateAdmin(adminId);

        PersonalRevenue revenue = personalRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        // Validate that this revenue belongs to the admin
        if (!revenue.getAdmin().getId().equals(admin.getId())) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        personalRevenueRepository.delete(revenue);
    }

    private PersonalRevenueDTO convertToDTO(PersonalRevenue revenue) {
        PersonalRevenueDTO dto = new PersonalRevenueDTO();
        BeanUtils.copyProperties(revenue, dto);
        dto.setAdminId(revenue.getAdmin().getId());
        dto.setAdminName(revenue.getAdmin().getName());
        return dto;
    }

    private User validateAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getAccountType() != AccountType.ADMIN) {
            throw new SecurityException("User is not an admin");
        }

        return admin;
    }
}