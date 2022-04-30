package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParticipantResponse {
    private Long id;

    private UserResponse user;

    private Double splitShare;

    public ParticipantResponse(Participant participant) {
        this.id = participant.getId();
        this.user = new UserResponse(participant.getUser());
        this.splitShare = participant.getSplitShare();
    }
}
