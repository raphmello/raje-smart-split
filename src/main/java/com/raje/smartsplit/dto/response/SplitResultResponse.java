package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.SplitResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SplitResultResponse {
    private String result;

    public SplitResultResponse(SplitResult splitResult) {
        String participant = splitResult.getParticipant().getUser().getUsername();
        Double amount = splitResult.getAmount();
        String debtType = splitResult.getDebtType().getDescription();
        this.result = String.format("%s %s R$ %.2f",participant, debtType, amount);
    }
}
