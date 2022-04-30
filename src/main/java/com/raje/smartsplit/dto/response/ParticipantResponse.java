package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
public class ParticipantResponse {
    private Long id;

    private AppUserResponse user;

    private Double splitShare;

    public ParticipantResponse(Participant participant) {
        this.id = participant.getId();
        this.user = new AppUserResponse(participant.getUser());
        this.splitShare = participant.getSplitShare();
    }
}
