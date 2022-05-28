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
import java.util.*;

@Service
public class SplitGroupService {

    private final SplitGroupRepository splitGroupRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipantRepository participantRepository;
    private final ParticipantService participantService;
    private final JwtUtils jwtUtils;

    @Autowired
    public SplitGroupService(SplitGroupRepository splitGroupRepository, UserService userService, CategoryService categoryService, ParticipantRepository participantRepository, ParticipantService participantService, JwtUtils jwtUtils) {
        this.splitGroupRepository = splitGroupRepository;
        this.userService = userService;
        this.categoryService = categoryService;
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
        if (optional.isEmpty())
            throw new GroupNotFountException("Group not found.");
        return new SplitGroupResponse(optional.get());
    }

    public SplitGroup getGroupByIdAndCurrentUser(Long groupId, Long userId) {
        Optional<SplitGroup> optional = splitGroupRepository.findByGroupIdAndParticipant(groupId, userId);
        if (optional.isEmpty())
            throw new GroupNotFountException("Group not found.");
        return optional.get();
    }

    public SplitGroup getGroupById(Long groupId) {
        Optional<SplitGroup> optional = splitGroupRepository.findById(groupId);
        if (optional.isEmpty())
            throw new GroupNotFountException("Group not found.");
        return optional.get();
    }

    public SplitGroupResponse addParticipantToGroup(User user, SplitGroup splitGroup) {
        SplitGroup entity = addParticipant(user, splitGroup);
        return new SplitGroupResponse(entity);
    }

    private SplitGroup addParticipant(User user, SplitGroup splitGroup) {
        Participant participant = createParticipant(user, splitGroup);
        splitGroup.addParticipant(participant);
        participantRepository.save(participant);
        return splitGroupRepository.save(splitGroup);
    }

    private Participant createParticipant(User user, SplitGroup splitGroup) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setSplitGroup(splitGroup);
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

    @Transactional
    public Participant updateCategories(Long groupId, List<Long> categories, User user) {
        SplitGroup group = getGroupById(groupId);
        Set<BillCategory> categoriesForGroup = categoryService.findAllByGroupId(groupId);
        Participant participant = participantService.findUserIfParticipantOfGroup(group, user);
        participant.setBillCategories(new HashSet<>());
        categories.forEach(c -> {
            BillCategory category = categoryService.findById(c);
            Optional<BillCategory> optionalCategory = categoriesForGroup.stream().filter(categ -> categ.equals(category)).findFirst();
            if (optionalCategory.isPresent()) {
                participant.addCategory(category);
            }
        });
        return participantRepository.save(participant);
    }

    public SplitGroupTotals getTotalAmountAndTotalShareForCategory(SplitGroup group, BillCategory category) {
        List<Double> groupShareList = new ArrayList<>();
        List<Double> groupAmountList = new ArrayList<>();
        group.getParticipants().forEach(participant -> {
            Double share = participant.getSplitShare();
            if (participantWantsToShareThisCategory(category, participant)) {
                groupShareList.add(share);
            }
            for (Bill bill : participant.getBills()) {
                if(bill.getCategory().equals(category)) {
                    final Double billAmount = bill.getAmount();
                    groupAmountList.add(billAmount);
                }
            }
        });

        double groupTotalShareSum = groupShareList.stream().mapToDouble(Double::doubleValue).sum();
        double groupTotalAmountSum = groupAmountList.stream().mapToDouble(Double::doubleValue).sum();

        return new SplitGroupTotals(groupTotalAmountSum, groupTotalShareSum);
    }

    public boolean participantWantsToShareThisCategory(BillCategory category, Participant participant) {
        return participant.getBillCategories().contains(category);
    }
}
