package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.ParticipantRepository;
import com.raje.smartsplit.repository.SplitExpensesGroupRepository;
import com.raje.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SplitExpensesGroupService {

    private final SplitExpensesGroupRepository splitExpensesGroupRepository;
    private final UserService userService;
    private final ParticipantRepository participantRepository;

    @Autowired
    public SplitExpensesGroupService(SplitExpensesGroupRepository splitExpensesGroupRepository, UserRepository userRepository, UserService userService, ParticipantRepository participantRepository) {
        this.splitExpensesGroupRepository = splitExpensesGroupRepository;
        this.userService = userService;
        this.participantRepository = participantRepository;
    }

    public SplitExpensesGroup createGroup(CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup group = new SplitExpensesGroup();
        group.setTitle(request.getTitle());
        User groupOwner = userService.getUserById(request.getUserId());
        group.setCreator(groupOwner);

        addParticipant(groupOwner, group);

        return splitExpensesGroupRepository.save(group);
    }

    public SplitExpensesGroupResponse getGroupById(Long groupId) {
        Optional<SplitExpensesGroup> optional = splitExpensesGroupRepository.findById(groupId);
        if (optional.isPresent())
            return new SplitExpensesGroupResponse(optional.get());
        throw new RuntimeException("Id not found.");
    }

    public SplitExpensesGroupResponse addParticipant(User appUser, SplitExpensesGroup splitExpensesGroup) {
        Participant participant = createParticipant(appUser, splitExpensesGroup);
        splitExpensesGroup.addParticipant(participant);
        SplitExpensesGroup entity = splitExpensesGroupRepository.save(splitExpensesGroup);
        return new SplitExpensesGroupResponse(entity);
    }

    private Participant createParticipant(User appUser, SplitExpensesGroup splitExpensesGroup) {
        Participant participant = new Participant();
        participant.setUser(appUser);
        if(userIsParticipatingInThisGroup(appUser, splitExpensesGroup)) {
            throw new RuntimeException("User is already in this group");
        }
        participant.setSplitExpensesGroup(splitExpensesGroup);
        splitExpensesGroupRepository.save(splitExpensesGroup);
        return participantRepository.save(participant);
    }

    private boolean userIsParticipatingInThisGroup(User appUser, SplitExpensesGroup splitExpensesGroup) {
        Optional<Participant> optionalParticipant =
                splitExpensesGroup.getParticipants()
                .stream()
                .filter(part -> part.getUser().getUsername().equals(appUser.getUsername())).findFirst();
        return optionalParticipant.isPresent();
    }

    public List<SplitExpensesGroup> findAll() {
        return splitExpensesGroupRepository.findAll();
    }
}
