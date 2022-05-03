package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SplitExpensesGroupService {

    private final SplitExpensesGroupRepository splitExpensesGroupRepository;
    private final UserService userService;
    private final ParticipantRepository participantRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public SplitExpensesGroupService(SplitExpensesGroupRepository splitExpensesGroupRepository, UserRepository userRepository, UserService userService, ParticipantRepository participantRepository, JwtUtils jwtUtils) {
        this.splitExpensesGroupRepository = splitExpensesGroupRepository;
        this.userService = userService;
        this.participantRepository = participantRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitExpensesGroup createGroup(CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup group = new SplitExpensesGroup();
        group.setTitle(request.getTitle());
        User currentUser = jwtUtils.getUserFromContext();

        User groupOwner = userService.getUserById(currentUser.getId());
        group.setCreator(groupOwner);

        addParticipantToGroup(groupOwner, group);

        return splitExpensesGroupRepository.save(group);
    }

    public SplitExpensesGroupResponse getGroupByIdAndCurrentUser(Long groupId) {
        Long userId = jwtUtils.getUserFromContext().getId();
        Optional<SplitExpensesGroup> optional = splitExpensesGroupRepository.findByGroupIdAndUserId(groupId, userId);
        return optional.map(SplitExpensesGroupResponse::new).orElse(null);
    }

    public SplitExpensesGroupResponse addParticipantToGroup(User user, SplitExpensesGroup splitExpensesGroup) {
        SplitExpensesGroup entity = addParticipant(user, splitExpensesGroup);
        return new SplitExpensesGroupResponse(entity);
    }

    private SplitExpensesGroup addParticipant(User user, SplitExpensesGroup splitExpensesGroup) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setSplitExpensesGroup(splitExpensesGroup);
        splitExpensesGroup.addParticipant(participant);
        participantRepository.save(participant);
        SplitExpensesGroup updatedGroup = splitExpensesGroupRepository.save(splitExpensesGroup);
        return updatedGroup;
    }

    public List<SplitExpensesGroup> findAll() {
        return splitExpensesGroupRepository.findAll();
    }

    public List<SplitExpensesGroup> findAllGroupsByUsername() {
        Long id = jwtUtils.getUserFromContext().getId();
        return splitExpensesGroupRepository.findByUserId(id);
    }
}
