package zenith_expense_tracker_nov_v1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.PropertyDTO;
import zenith_expense_tracker_nov_v1.entity.ArchivedExpenses;
import zenith_expense_tracker_nov_v1.entity.Expenses;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.repository.ArchivedExpensesRepository;
import zenith_expense_tracker_nov_v1.repository.ExpenseRepository;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ArchivedExpensesRepository archivedExpensesRepository;

    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    public Property createProperty(Property property) {
        Property createdProperty = propertyRepository.save(property);
        logger.info("Property created with ID: {}", createdProperty.getId());
        return createdProperty;
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        property.setName(propertyDetails.getName());
        property.setDescription(propertyDetails.getDescription());
        property.setAddress(propertyDetails.getAddress());

        Property updatedProperty = propertyRepository.save(property);
        logger.info("Property with ID: {} updated", updatedProperty.getId());
        return updatedProperty;
    }



    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        // Copy all associated expenses to archived_expenses
        List<Expenses> expenses = property.getExpenses();
        for (Expenses expense : expenses) {
            ArchivedExpenses archivedExpense = new ArchivedExpenses();
            archivedExpense.setAmount(expense.getAmount());
            archivedExpense.setCreatedAt(expense.getCreatedAt());
            archivedExpense.setCreatedBy(expense.getCreatedBy());
            archivedExpense.setDate(expense.getDate());
            archivedExpense.setDescription(expense.getDescription());
            archivedExpense.setPropertyName(expense.getPropertyName());
            archivedExpense.setExpenseType(expense.getExpenseType());
            archivedExpense.setGenericExpenses(expense.getGenericExpenses());

            archivedExpensesRepository.save(archivedExpense); // Save to archived_expenses
        }

        // Disassociate expenses from the property (optional)
        for (Expenses expense : expenses) {
            expense.setProperty(null);
            expenseRepository.save(expense);
        }

        // Delete the property
        propertyRepository.deleteById(id);
        logger.info("Property with ID: {} deleted. Expenses archived and disassociated.", id);
    }



    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        logger.info("Retrieved {} properties", properties.size());
        return properties.stream().map(property -> new PropertyDTO(
                property.getId(),
                property.getName(),
                property.getAddress(),
                property.getDescription()
        )).collect(Collectors.toList());
    }
}
