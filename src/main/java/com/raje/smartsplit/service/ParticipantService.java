package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.exception.NotParticipantException;
import com.raje.smartsplit.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository repository;

    @Autowired
    public ParticipantService(ParticipantRepository repository) {
        this.repository = repository;
    }

    Participant findByGroupIdAndUserId(Long groupId, Long userId) {
        Optional<Participant> optional = repository.findByGroupIdAndUserId(groupId, userId);
        if (optional.isEmpty())
            throw new RuntimeException("User is not a participant in this group.");

        return optional.get();
    }

    public Participant findUserIfParticipantOfGroup(SplitGroup group, User user) {
        Optional<Participant> optional = group.getParticipants().stream().filter(p -> p.getUser().equals(user)).findFirst();
        if (optional.isEmpty())
            throw new NotParticipantException();
        return optional.get();
    }
}
