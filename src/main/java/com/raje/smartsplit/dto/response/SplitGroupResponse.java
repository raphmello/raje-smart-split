package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.SplitGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SplitGroupResponse {
    private Long id;
    private String title;
    private LocalDate creationDate;
    private UserResponse creator;
    private List<ParticipantResponse> participants;

    public SplitGroupResponse(SplitGroup entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.creationDate = entity.getCreationDate();
        this.creator = new UserResponse(entity.getCreator());
        this.participants = new ArrayList<>();
        entity.getParticipants().forEach(participant -> participants.add(new ParticipantResponse(participant)));
    }
}
