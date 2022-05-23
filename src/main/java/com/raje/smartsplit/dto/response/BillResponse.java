package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BillResponse {
    private Long id;

    private String description;

    private BillCategoryResponse category;

    private Double amount;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.description = bill.getDescription();
        this.category = new BillCategoryResponse(bill.getCategory());
        this.amount = bill.getAmount();
    }
}
