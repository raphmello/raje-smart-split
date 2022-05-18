package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.request.CreateBillRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GroupManagementService {

    private final BillRepository billRepository;
    private final ParticipantService participantService;
    private final SplitGroupService groupService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GroupManagementService(BillRepository billRepository,
                                  ParticipantService participantService, SplitGroupService groupService,
                                  JwtUtils jwtUtils) {
        this.billRepository = billRepository;
        this.participantService = participantService;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitGroupResponse participateInTheGroup(Long groupId) {
        User user = jwtUtils.getUserFromContext();
        SplitGroup group = groupService.getGroupById(groupId);
        return groupService.addParticipantToGroup(user, group);
    }

    @Transactional
    public SplitGroupResponse addBillToParticipant(Long groupId, CreateBillRequest billRequest) {
        User user = jwtUtils.getUserFromContext();

        SplitGroup group = groupService.getGroupById(groupId);

        Bill bill = billRequest.requestToEntity();
        bill.setUser(user);

        Participant participant = participantService.findUserIfParticipantOfGroup(group, user);
        participant.addBill(bill);
        bill.setParticipant(participant);

        billRepository.save(bill);

        return new SplitGroupResponse(group);
    }

    public void exitTheGroup(Long groupId) {
        Long userId = jwtUtils.getUserFromContext().getId();
        groupService.removeCurrentUserFromGroup(groupId, userId);
    }
}
