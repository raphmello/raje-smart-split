package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.response.ParticipantResponse;
import com.raje.smartsplit.dto.response.ParticipantSplitGroupResponse;
import com.raje.smartsplit.dto.response.SplitResultResponse;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.exception.NotParticipantException;
import com.raje.smartsplit.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final SplitResultService splitResultService;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, SplitResultService splitResultService) {
        this.participantRepository = participantRepository;
        this.splitResultService = splitResultService;
    }

    public Participant findByGroupIdAndUserId(Long groupId, Long userId) {
        Optional<Participant> optional = participantRepository.findByGroupIdAndUserId(groupId, userId);
        if (optional.isEmpty())
            throw new RuntimeException("User is not a participant in this group.");

        return optional.get();
    }

    public Participant findParticipantIfUserIsParticipantOfGroup(SplitGroup group, User user) {
        Optional<Participant> optional = group.getParticipants().stream().filter(p -> p.getUser().equals(user)).findFirst();
        if (optional.isEmpty())
            throw new NotParticipantException();
        return optional.get();
    }

    @Transactional
    public ParticipantSplitGroupResponse updateSplitShare(Long participantId, Double splitShare, User currentUser) {
        Participant participant = findById(participantId);
        if(currentUserIsParticipant(participant, currentUser)) {
            participant.setSplitShare(splitShare);
            participantRepository.save(participant);
            List<SplitResult> splitResults = splitResultService.updateSplitResult(participant.getSplitGroup());
            List<SplitResultResponse> splitResultResponses = new ArrayList<>();
            splitResults.forEach(sr -> {
                splitResultResponses.add(new SplitResultResponse(sr));
            });
            return new ParticipantSplitGroupResponse(new ParticipantResponse(participant),splitResultResponses);
        } else {
            throw new RuntimeException("This user cannot change the share from another user.");
        }
    }

    private boolean currentUserIsParticipant(Participant participant, User currentUser) {
        return participant.getUser().equals(currentUser);
    }

    public Participant findById(Long participantId) {
        Optional<Participant> optional = participantRepository.findById(participantId);
        if (optional.isEmpty())
            throw new RuntimeException("Participant not found.");

        return optional.get();
    }
}
