package zenith_expense_tracker_nov_v1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.PropertyRevenueDTO;
import zenith_expense_tracker_nov_v1.entity.*;
import zenith_expense_tracker_nov_v1.repository.PropertyRevenueRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PropertyRevenueServiceImpl implements PropertyRevenueService {

    private final PropertyRevenueRepository propertyRevenueRepository;
    private final UserRepository userRepository;

    @Override
    public PropertyRevenueDTO createRevenue(PropertyRevenueDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PropertyRevenue revenue = new PropertyRevenue();
        BeanUtils.copyProperties(dto, revenue);
        revenue.setDateTime(LocalDateTime.now());
        revenue.setUser(user);

        PropertyRevenue savedRevenue = propertyRevenueRepository.save(revenue);
        return convertToDTO(savedRevenue);
    }



    @Override
    public PropertyRevenueDTO getRevenueById(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PropertyRevenue revenue = propertyRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        // Validate access
        if (!canAccessRevenue(user, revenue)) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        return convertToDTO(revenue);
    }



    @Override
    public PropertyRevenueDTO updateRevenue(Long id, PropertyRevenueDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PropertyRevenue revenue = propertyRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        if (!canAccessRevenue(user, revenue)) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        if ("BILL".equals(revenue.getSource())) {
            throw new IllegalStateException("Cannot update bill-generated revenue");
        }

        revenue.setAmount(dto.getAmount());
        revenue.setDescription(dto.getDescription());
        revenue.setPaymentMode(dto.getPaymentMode());
        revenue.setSource(dto.getSource());
        PropertyRevenue updatedRevenue = propertyRevenueRepository.save(revenue);
        return convertToDTO(updatedRevenue);
    }

    @Override
    public void deleteRevenue(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PropertyRevenue revenue = propertyRevenueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue not found"));

        if (!canAccessRevenue(user, revenue)) {
            throw new SecurityException("Unauthorized access to revenue");
        }

        if ("BILL".equals(revenue.getSource())) {
            throw new IllegalStateException("Cannot delete bill-generated revenue");
        }

        propertyRevenueRepository.delete(revenue);
    }

    private PropertyRevenueDTO convertToDTO(PropertyRevenue revenue) {
        PropertyRevenueDTO dto = new PropertyRevenueDTO();
        BeanUtils.copyProperties(revenue, dto);
        dto.setUserId(revenue.getUser().getId());
        dto.setUserName(revenue.getUser().getName());
        return dto;
    }

    private boolean canAccessRevenue(User user, PropertyRevenue revenue) {
        if (user.getAccountType() == AccountType.ADMIN) {
            return true;
        }
        return user.getId().equals(revenue.getUser().getId());
    }

    @Override
    public List<PropertyRevenueDTO> getAllRevenues(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getAccountType() != AccountType.ADMIN) {
            throw new SecurityException("Only admin can access all revenues");
        }

        return propertyRevenueRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
