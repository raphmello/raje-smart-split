package com.raje.smartsplit.dto.partial;

import com.raje.smartsplit.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantAmountSummary {
    private Participant participant;
    private List<Double> amountList;

    public Double getAmountSum() {
        return amountList.stream().mapToDouble(Double::doubleValue).sum();
    }
}
