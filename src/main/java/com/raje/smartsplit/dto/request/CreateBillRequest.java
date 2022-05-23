package com.raje.smartsplit.dto.request;

import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.BillCategory;
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
    private Long categoryId;

    @NotNull
    private Double amount;

    public Bill requestToEntity(BillCategory category) {
        Bill bill = new Bill();
        bill.setDescription(this.description);
        bill.setCategory(category);
        bill.setAmount(this.amount);
        return bill;
    }
}
