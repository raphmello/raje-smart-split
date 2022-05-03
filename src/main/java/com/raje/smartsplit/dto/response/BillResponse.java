package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
public class BillResponse {
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    private Double amount;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.description = bill.getDescription();
        this.category = bill.getCategory();
        this.amount = bill.getAmount();
    }
}
