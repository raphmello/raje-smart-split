package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.partial.SplitGroupTotals;
import com.raje.smartsplit.entity.*;
import com.raje.smartsplit.enums.EDebtType;
import com.raje.smartsplit.repository.SplitResultRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SplitResultService {

    private final SplitResultRepository repository;
    private final SplitGroupService groupService;
    private final CategoryService categoryService;

    public SplitResultService(SplitResultRepository repository, SplitGroupService groupService, CategoryService categoryService) {
        this.repository = repository;
        this.groupService = groupService;
        this.categoryService = categoryService;
    }

    private List<SplitResult> calculatesDebtorsAndCreditorsByCategory(SplitGroup group, BillCategory category) {
        SplitGroupTotals totals = groupService.getTotalAmountAndTotalShareForCategory(group, category);

        List<SplitResult> splitResultList = new ArrayList<>();

        group.getParticipants()
                .forEach(participant -> {
                    if(groupService.participantWantsToShareThisCategory(category, participant)) {
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
        repository.saveAll(splitResultList);
    }

    private void deletePreviousSplitResults(SplitGroup group) {
        List<SplitResult> splitResults = repository.findAllBySplitGroup(group.getId());
        if (splitResults.isEmpty()) {
            return;
        }
        repository.deleteAll(splitResults);
    }

    private Double calculateAmountShareForParticipant(Participant participant, double amountSum, double shareSum) {
        Double userShare = participant.getSplitShare();
        return (userShare/shareSum) * amountSum;
    }

    public List<SplitResult> updateSplitResult(SplitGroup group) {
        Set<BillCategory> groupCategories = categoryService.findAllByGroupId(group.getId());
        List<SplitResult> splitResults = generateSplitResults(group, groupCategories);
        saveMultipleSplitResult(group, splitResults);
        return splitResults;
    }

    private List<SplitResult> generateSplitResults(SplitGroup group, Set<BillCategory> groupCategories) {
        List<SplitResult> splitResults = new ArrayList<>();
        groupCategories.forEach(category -> {
            List<SplitResult> partialList = calculatesDebtorsAndCreditorsByCategory(group, category);
            splitResults.addAll(partialList);
        });
        return splitResults;
    }

    public List<SplitResult> updateSplitResult(Long groupId) {
        SplitGroup group = groupService.getGroupById(groupId);
        return updateSplitResult(group);
    }
}
