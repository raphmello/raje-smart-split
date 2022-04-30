package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SplitExpensesGroupResponse {
    private Long id;
    private String title;
    private List<BillResponse> bills;
    private List<ParticipantResponse> participants;

    public SplitExpensesGroupResponse(SplitExpensesGroup entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();

        this.bills = new ArrayList<>();
        entity.getBills().forEach(bill -> bills.add(new BillResponse(bill)));

        this.participants = new ArrayList<>();
        entity.getParticipants().forEach(participant -> participants.add(new ParticipantResponse(participant)));
    }
}
