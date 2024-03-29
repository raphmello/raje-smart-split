package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.partial.SplitGroupTotals;
import com.raje.smartsplit.dto.response.ParticipantResponse;
import com.raje.smartsplit.dto.response.ParticipantSplitGroupResponse;
import com.raje.smartsplit.dto.response.SplitResultResponse;
import com.raje.smartsplit.dto.utils.splitresult.DataDebtor;
import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.enums.EDebtType;
import com.raje.smartsplit.repository.ParticipantRepository;
import com.raje.smartsplit.repository.SplitSimplificationResultRepository;
import com.raje.smartsplit.repository.splitresult.SplitResultRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SplitResultService {

    private final SplitResultRepository splitResultRepository;
    private final SplitSimplificationResultRepository simplificationResultRepository;
    private final SplitGroupService groupService;
    private final CategoryService categoryService;
    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;

    public SplitResultService(SplitResultRepository splitResultRepository,
                              SplitSimplificationResultRepository simplificationResultRepository,
                              SplitGroupService groupService, CategoryService categoryService,
                              ParticipantService participantService,
                              ParticipantRepository participantRepository) {
        this.splitResultRepository = splitResultRepository;
        this.simplificationResultRepository = simplificationResultRepository;
        this.groupService = groupService;
        this.categoryService = categoryService;
        this.participantService = participantService;
        this.participantRepository = participantRepository;
    }

    private List<SplitResult> calculatesDebtorsAndCreditorsByCategory(SplitGroup group, BillCategory category) {
        SplitGroupTotals totals = groupService.getTotalAmountAndTotalShareForCategory(group, category);

        List<SplitResult> splitResultList = new ArrayList<>();

        group.getParticipants()
                .forEach(participant -> {
                    if (groupService.participantWantsToShareThisCategory(category, participant) && participant.getSplitShare() > 0) {
                        Double splitValue = calculateAmountShareForParticipant(participant, totals.getTotalAmount(), totals.getTotalShare());
                        double valueBalance = participant.getTotalSpentByCategory(category) - splitValue;
                        if (valueBalance != 0) {
                            SplitResult sr = SplitResult.builder()
                                    .amount(Math.abs(valueBalance))
                                    .debtType(EDebtType.getDebtType(valueBalance))
                                    .splitGroup(group)
                                    .participant(participant)
                                    .category(category)
                                    .build();
                            splitResultList.add(sr);
                        }
                    }
                });

        return splitResultList;
    }



    @Transactional
    public void saveMultipleSplitResult(SplitGroup group, List<SplitResult> splitResultList) {
        deletePreviousSplitResults(group);
        splitResultRepository.saveAll(splitResultList);
    }

    private void deletePreviousSplitResults(SplitGroup group) {
        List<SplitResult> splitResults = splitResultRepository.findAllBySplitGroup(group.getId());
        if (splitResults.isEmpty()) {
            return;
        }
        splitResultRepository.deleteAll(splitResults);
    }

    private Double calculateAmountShareForParticipant(Participant participant, double amountSum, double shareSum) {
        Double userShare = participant.getSplitShare();
        return (userShare / shareSum) * amountSum;
    }

    public List<SplitResult> updateSplitResult(SplitGroup group) {
        Set<BillCategory> groupCategories = categoryService.findAllByGroupId(group.getId());
        List<SplitResult> splitResults = generateSplitResults(group, groupCategories);
        saveMultipleSplitResult(group, splitResults);
        return splitResults;
    }

    private List<SplitResult> generateSplitResults(SplitGroup group, Set<BillCategory> groupCategories) {
        return groupCategories.stream()
                .map(category -> calculatesDebtorsAndCreditorsByCategory(group, category))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<SplitResult> updateSplitResult(Long groupId) {
        SplitGroup group = groupService.getGroupById(groupId);
        return updateSplitResult(group);
    }

    @Transactional
    public ParticipantSplitGroupResponse updateCategories(Long groupId, List<Long> categories, User user) {
        SplitGroup group = groupService.getGroupById(groupId);
        Set<BillCategory> categoriesForGroup = categoryService.findAllByGroupId(groupId);
        Participant participant = participantService.findParticipantIfUserIsParticipantOfGroup(group, user);
        participant.setBillCategories(new HashSet<>());
        categories.forEach(c -> {
            BillCategory category = categoryService.findById(c);
            Optional<BillCategory> optionalCategory = categoriesForGroup.stream().filter(categ -> categ.equals(category)).findFirst();
            if (optionalCategory.isPresent()) {
                participant.addCategory(category);
            }
        });
        List<SplitResult> updatedSplitResultList = updateSplitResult(group);
        List<SplitResultResponse> splitResultResponses = new ArrayList<>();
        updatedSplitResultList.forEach(splitResult -> splitResultResponses.add(new SplitResultResponse(splitResult)));
        Participant savedParticipant = participantRepository.save(participant);
        return new ParticipantSplitGroupResponse(new ParticipantResponse(savedParticipant), splitResultResponses);
    }

    public List<DataDebtor> findAllParticipantsOrderByBiggestDebtorsAsc(Long groupId) {
        List<Object[]> debtorsAsc = simplificationResultRepository.findAllBySplitGroupOrderByBiggestDebtorsAsc(groupId);
        List<DataDebtor> dataDebtors = new ArrayList<>();
        for (Object[] debtor : debtorsAsc) {
            DataDebtor dataDebtor = new DataDebtor();
            dataDebtor.setId(((BigInteger) debtor[0]).longValue());
            dataDebtor.setUsername((String) debtor[1]);
            dataDebtor.setAmountSum((Double) debtor[2]);
            dataDebtors.add(dataDebtor);
        }
        return dataDebtors;
    }

    @Transactional
    public ParticipantSplitGroupResponse updateSplitShare(Long participantId, Double splitShare, User currentUser) {
        Participant participant = participantService.findById(participantId);
        if (currentUserIsParticipant(participant, currentUser)) {
            participant.setSplitShare(splitShare);
            participantRepository.save(participant);
            List<SplitResult> splitResults = updateSplitResult(participant.getSplitGroup());
            List<SplitResultResponse> splitResultResponses = new ArrayList<>();
            splitResults.forEach(sr -> {
                splitResultResponses.add(new SplitResultResponse(sr));
            });
            return new ParticipantSplitGroupResponse(new ParticipantResponse(participant), splitResultResponses);
        } else {
            throw new RuntimeException("This user cannot change the share from another user.");
        }
    }

    private boolean currentUserIsParticipant(Participant participant, User currentUser) {
        return participant.getUser().equals(currentUser);
    }

}
