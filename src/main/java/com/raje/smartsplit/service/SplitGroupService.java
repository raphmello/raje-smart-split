package com.raje.smartsplit.service;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.partial.SplitGroupTotals;
import com.raje.smartsplit.dto.request.CreateSplitGroupRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.entity.*;
import com.raje.smartsplit.exception.GroupNotFountException;
import com.raje.smartsplit.exception.NotAllowedToExitException;
import com.raje.smartsplit.repository.ParticipantRepository;
import com.raje.smartsplit.repository.SplitGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SplitGroupService {

    private final SplitGroupRepository splitGroupRepository;
    private final UserService userService;
    private final ParticipantRepository participantRepository;
    private final ParticipantService participantService;
    private final JwtUtils jwtUtils;

    @Autowired
    public SplitGroupService(SplitGroupRepository splitGroupRepository, UserService userService, ParticipantRepository participantRepository, ParticipantService participantService, JwtUtils jwtUtils) {
        this.splitGroupRepository = splitGroupRepository;
        this.userService = userService;
        this.participantRepository = participantRepository;
        this.participantService = participantService;
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

    public SplitGroupResponse getGroupResponseByIdAndCurrentUser(Long groupId, Long userId) {
        Optional<SplitGroup> optional = splitGroupRepository.findByGroupIdAndParticipant(groupId, userId);

        SplitGroup group = optional.orElseThrow(() -> new GroupNotFountException("Group not found."));

        return new SplitGroupResponse(group);
    }

    public SplitGroup getGroupByIdAndCurrentUser(Long groupId, Long userId) {
        Optional<SplitGroup> optional = splitGroupRepository.findByGroupIdAndParticipant(groupId, userId);

        return optional.orElseThrow(() -> new GroupNotFountException("Group not found."));
    }

    public SplitGroup getGroupById(Long groupId) {
        Optional<SplitGroup> optional = splitGroupRepository.findById(groupId);
        return optional.orElseThrow(() -> new GroupNotFountException("Group not found."));
    }

    public SplitGroupResponse addParticipantToGroup(User user, SplitGroup splitGroup) {
        SplitGroup entity = addParticipant(user, splitGroup, 1.);
        return new SplitGroupResponse(entity);
    }

    public SplitGroupResponse addParticipantToGroup(User user, SplitGroup splitGroup, Double splitShare) {
        SplitGroup entity = addParticipant(user, splitGroup, splitShare);
        return new SplitGroupResponse(entity);
    }

    private SplitGroup addParticipant(User user, SplitGroup splitGroup, Double splitShare) {
        Participant participant = createParticipant(user, splitGroup, splitShare);
        splitGroup.addParticipant(participant);
        participantRepository.save(participant);
        return splitGroupRepository.save(splitGroup);
    }

    private Participant createParticipant(User user, SplitGroup splitGroup, Double splitShare) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setSplitGroup(splitGroup);
        participant.setSplitShare(splitShare);
        return participant;
    }

    public List<SplitGroup> findAll() {
        return splitGroupRepository.findAll();
    }

    public List<SplitGroup> findAllGroupsByUsername() {
        Long id = jwtUtils.getUserFromContext().getId();
        return splitGroupRepository.findByUserId(id);
    }

    @Transactional
    public void removeCurrentUserFromGroup(Long groupId, Long userId) {
        SplitGroup group = getGroupByIdAndCurrentUser(groupId, userId);
        User currentUser = jwtUtils.getUserFromContext();

        if (userIsTheOwner(group, currentUser))
            throw new NotAllowedToExitException();

        removeCurrentUserFromGroup(groupId, group, currentUser);
    }

    private void removeCurrentUserFromGroup(Long groupId, SplitGroup group, User currentUser) {
        group.removeParticipant(currentUser);
        splitGroupRepository.save(group);

        Participant participant = participantService.findByGroupIdAndUserId(groupId, currentUser.getId());
        participantRepository.delete(participant);
    }

    private boolean userIsTheOwner(SplitGroup group, User user) {
        return group.getCreator().equals(user);
    }

    public SplitGroupTotals getTotalAmountAndTotalShareForCategory(SplitGroup group, BillCategory category) {

        List<Double> groupShareFractionList = group.getParticipants().stream()
                .filter(participant -> participantWantsToShareThisCategory(category, participant))
                .map(Participant::getSplitShare)
                .collect(Collectors.toList());

        List<Double> groupAmountListByCategory = group.getParticipants().stream()
                .map(Participant::getBills)
                .map(bills -> retrieveBillValuesByCategory(category, bills))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        double groupTotalShareSum = groupShareFractionList.stream().mapToDouble(Double::doubleValue).sum();
        double groupTotalAmountSum = groupAmountListByCategory.stream().mapToDouble(Double::doubleValue).sum();

        return new SplitGroupTotals(groupTotalAmountSum, groupTotalShareSum);
    }

    private List<Double> retrieveBillValuesByCategory(BillCategory category, List<Bill> bills) {
        return bills.stream()
                .filter(bill -> bill.getCategory().equals(category))
                .map(Bill::getAmount)
                .collect(Collectors.toList());
    }

    public boolean participantWantsToShareThisCategory(BillCategory category, Participant participant) {
        return participant.getBillCategories().contains(category);
    }
}
