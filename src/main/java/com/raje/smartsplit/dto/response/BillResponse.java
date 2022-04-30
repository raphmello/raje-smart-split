package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BillResponse {
    private Long id;

    private AppUserResponse appUser;

    private Double amount;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.appUser.setId(bill.getAppUser().getId());
        this.appUser.setUsername(bill.getAppUser().getUsername());
        this.amount = bill.getAmount();
    }
}
