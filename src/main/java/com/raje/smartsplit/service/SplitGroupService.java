package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.request.CreateSplitGroupRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.ParticipantRepository;
import com.raje.smartsplit.repository.SplitGroupRepository;
import com.raje.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SplitGroupService {

    private final SplitGroupRepository splitGroupRepository;
    private final UserService userService;
    private final ParticipantRepository participantRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public SplitGroupService(SplitGroupRepository splitGroupRepository, UserRepository userRepository, UserService userService, ParticipantRepository participantRepository, JwtUtils jwtUtils) {
        this.splitGroupRepository = splitGroupRepository;
        this.userService = userService;
        this.participantRepository = participantRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitGroup createGroup(CreateSplitGroupRequest request) {
        SplitGroup group = new SplitGroup();
        group.setTitle(request.getTitle());
        User currentUser = jwtUtils.getUserFromContext();

        User groupOwner = userService.getUserById(currentUser.getId());
        group.setCreator(groupOwner);

        addParticipantToGroup(groupOwner, group);

        return splitGroupRepository.save(group);
    }

    public SplitGroupResponse getGroupByIdAndCurrentUser(Long groupId) {
        Long userId = jwtUtils.getUserFromContext().getId();
        Optional<SplitGroup> optional = splitGroupRepository.findByGroupIdAndUserId(groupId, userId);
        return optional.map(SplitGroupResponse::new).orElse(null);
    }

    public SplitGroupResponse addParticipantToGroup(User user, SplitGroup splitGroup) {
        SplitGroup entity = addParticipant(user, splitGroup);
        return new SplitGroupResponse(entity);
    }

    private SplitGroup addParticipant(User user, SplitGroup splitGroup) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setSplitGroup(splitGroup);
        splitGroup.addParticipant(participant);
        participantRepository.save(participant);
        return splitGroupRepository.save(splitGroup);
    }

    public List<SplitGroup> findAll() {
        return splitGroupRepository.findAll();
    }

    public List<SplitGroup> findAllGroupsByUsername() {
        Long id = jwtUtils.getUserFromContext().getId();
        return splitGroupRepository.findByUserId(id);
    }
}
