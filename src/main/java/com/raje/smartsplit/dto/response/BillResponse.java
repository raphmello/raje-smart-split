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

    private UserResponse user;

    private Double amount;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.user.setId(bill.getUser().getId());
        this.user.setUsername(bill.getUser().getUsername());
        this.amount = bill.getAmount();
    }
}
