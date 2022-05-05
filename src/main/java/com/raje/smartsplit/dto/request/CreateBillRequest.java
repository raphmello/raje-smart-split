package com.raje.smartsplit.dto.request;

import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.enums.ECategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillRequest {
    @NotBlank
    private String description;

    @NotNull
    private ECategory category = ECategory.GENERAL;

    @NotNull
    private Double amount;

    public Bill requestToEntity() {
        Bill bill = new Bill();
        bill.setDescription(this.description);
        bill.setCategory(this.category);
        bill.setAmount(this.amount);
        return bill;
    }
}
