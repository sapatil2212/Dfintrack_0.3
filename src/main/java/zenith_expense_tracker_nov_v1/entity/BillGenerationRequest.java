package zenith_expense_tracker_nov_v1.entity;

import lombok.Data;

@Data
public class BillGenerationRequest {
    private Long bookingId;
    private Double amount;
    private Double totalAmount;
    private Double cgst;
    private Double sgst;
private String modeOfPayment;

}