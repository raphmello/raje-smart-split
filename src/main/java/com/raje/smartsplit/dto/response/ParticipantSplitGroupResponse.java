package com.raje.smartsplit.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParticipantSplitGroupResponse {
    private ParticipantResponse participant;
    private List<SplitResultResponse> splitResults;

    public ParticipantSplitGroupResponse(ParticipantResponse participant, List<SplitResultResponse> result) {
        this.participant = participant;
        this.splitResults = result;
    }
}
