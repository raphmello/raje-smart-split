package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ParticipantResponse {
    private Long id;

    private UserResponse user;

    private Double splitShare;

    private List<BillResponse> bills = new ArrayList<>();

    private Set<BillCategoryResponse> categories = new HashSet<>();

    public ParticipantResponse(Participant participant) {
        this.id = participant.getId();
        this.user = new UserResponse(participant.getUser());
        this.splitShare = participant.getSplitShare();
        participant.getBills().forEach(bill -> bills.add(new BillResponse(bill)));
        participant.getBillCategories().forEach(category -> categories.add(new BillCategoryResponse(category)));
    }
}
