package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.request.CreateBillRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.repository.BillRepository;
import com.raje.smartsplit.repository.SplitGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupManagementService {

    private final BillRepository billRepository;
    private final SplitGroupRepository groupRepository;
    private final SplitGroupService groupService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GroupManagementService(BillRepository billRepository,
                                  SplitGroupService groupService,
                                  SplitGroupRepository groupRepository,
                                  JwtUtils jwtUtils) {
        this.billRepository = billRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public SplitGroupResponse participateInTheGroup(Long groupId) {
        User user = jwtUtils.getUserFromContext();
        Optional<SplitGroup> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isEmpty())
            throw new RuntimeException("Group not found");

        return groupService.addParticipantToGroup(user, optionalGroup.get());
    }

    @Transactional
    public SplitGroupResponse addBillToParticipant(Long groupId, CreateBillRequest billRequest) {
        User user = jwtUtils.getUserFromContext();

        Optional<SplitGroup> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty())
            throw new RuntimeException("Group not found.");

        SplitGroup group = optionalGroup.get();

        Bill bill = billRequest.requestToEntity();
        bill.setUser(user);

        Participant participant = getParticipantByUser(group, user);
        participant.addBill(bill);
        bill.setParticipant(participant);

        billRepository.save(bill);

        return new SplitGroupResponse(group);
    }

    private Participant getParticipantByUser(SplitGroup group, User user) {
        Optional<Participant> optional = group.getParticipants().stream().filter(p -> p.getUser().equals(user)).findFirst();
        if (optional.isEmpty())
            throw new RuntimeException("User is not a participant");
        return optional.get();
    }

    public void exitTheGroup(Long groupId) {
        groupService.removeCurrentUserFromGroup(groupId);
    }
}
