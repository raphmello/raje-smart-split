package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.request.CreateBillRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.dto.response.SplitGroupSplitResultResponse;
import com.raje.smartsplit.dto.response.SplitResultResponse;
import com.raje.smartsplit.entity.*;
import com.raje.smartsplit.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupManagementService {

    private final BillRepository billRepository;
    private final ParticipantService participantService;
    private final SplitGroupService groupService;
    private final SplitResultService splitResultService;
    private final CategoryService categoryService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GroupManagementService(BillRepository billRepository,
                                  ParticipantService participantService, SplitGroupService groupService,
                                  SplitResultService splitResultService, CategoryService categoryService, JwtUtils jwtUtils) {
        this.billRepository = billRepository;
        this.participantService = participantService;
        this.groupService = groupService;
        this.splitResultService = splitResultService;
        this.categoryService = categoryService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitGroupResponse participateInTheGroup(Long groupId) {
        User user = jwtUtils.getUserFromContext();
        SplitGroup group = groupService.getGroupById(groupId);
        return groupService.addParticipantToGroup(user, group);
    }

    @Transactional
    public SplitGroupSplitResultResponse addBillToParticipant(Long groupId, CreateBillRequest billRequest) {
        User user = jwtUtils.getUserFromContext();

        SplitGroup group = groupService.getGroupById(groupId);
        BillCategory category = categoryService.findById(billRequest.getCategoryId());

        Bill bill = billRequest.requestToEntity(category);
        bill.setUser(user);

        Participant participant = participantService.findParticipantIfUserIsParticipantOfGroup(group, user);
        participant.addBill(bill);
        bill.setParticipant(participant);

        billRepository.save(bill);

        List<SplitResult> updatedSplitResultList = splitResultService.updateSplitResult(group);

        List<SplitResultResponse> splitResultResponses = new ArrayList<>();

        updatedSplitResultList.forEach(splitResult -> splitResultResponses.add(new SplitResultResponse(splitResult)));

        return new SplitGroupSplitResultResponse(new SplitGroupResponse(group), splitResultResponses);
    }

    public void exitTheGroup(Long groupId) {
        Long userId = jwtUtils.getUserFromContext().getId();
        groupService.removeCurrentUserFromGroup(groupId, userId);
    }
}
