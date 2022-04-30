package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.AppUserRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.repository.AppUserRepository;
import com.raje.smartsplit.repository.SplitExpensesGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupManagementService {

    private final AppUserRepository userRepository;
    private final SplitExpensesGroupRepository groupRepository;
    private final SplitExpensesGroupService groupService;

    @Autowired
    public GroupManagementService(AppUserRepository userRepository, SplitExpensesGroupService groupService, SplitExpensesGroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    public SplitExpensesGroupResponse addParticipantToGroup(AppUserRequest userRequest, Long groupId) {
        Optional<AppUser> optionalUser = userRepository.findById(userRequest.getUserId());
        Optional<SplitExpensesGroup> optionalGroup = groupRepository.findById(groupId);

        if (optionalUser.isEmpty())
            throw new RuntimeException("User not found");

        if (optionalGroup.isEmpty())
            throw new RuntimeException("Group not found");

        return groupService.addParticipant(optionalUser.get(), optionalGroup.get());
    }
}