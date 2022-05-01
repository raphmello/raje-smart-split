package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.SplitExpensesGroupRepository;
import com.raje.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupManagementService {

    private final UserRepository userRepository;
    private final SplitExpensesGroupRepository groupRepository;
    private final SplitExpensesGroupService groupService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GroupManagementService(UserRepository userRepository, SplitExpensesGroupService groupService, SplitExpensesGroupRepository groupRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitExpensesGroupResponse participateInTheGroup(Long groupId) {
        Optional<User> optionalUser = jwtUtils.getUserFromContext();
        Optional<SplitExpensesGroup> optionalGroup = groupRepository.findById(groupId);

        if (optionalUser.isEmpty())
            throw new RuntimeException("User not found");

        if (optionalGroup.isEmpty())
            throw new RuntimeException("Group not found");

        return groupService.addParticipantToGroup(optionalUser.get(), optionalGroup.get());
    }
}
