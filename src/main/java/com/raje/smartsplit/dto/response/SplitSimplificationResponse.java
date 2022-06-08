package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.SplitSimplificationResult;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.enums.EDebtType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SplitSimplificationResponse {

    private String debtor;
    private String creditor;
    private Double amount;
    private String result;

    public SplitSimplificationResponse(SplitSimplificationResult splitSimplificationResult) {
        this.debtor = splitSimplificationResult.getDebtor().getUsername();
        this.creditor = splitSimplificationResult.getCreditor().getUsername();
        this.amount = splitSimplificationResult.getAmount();
        this.result = String.format("%s %s R$ %.2f para %s",this.debtor, EDebtType.DEBTOR.getDescription(), this.amount, this.creditor);
    }
}
