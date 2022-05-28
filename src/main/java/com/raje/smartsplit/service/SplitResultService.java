package com.raje.smartsplit.service;

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

        List<SplitResult> splitResultList = new ArrayList<>();

        group.getParticipants()
                .forEach(participant -> {
                    if(participantWantsToShareThisCategory(category, participant)) {
                        Double splitValue = calculateAmountShareForParticipant(participant, groupTotalAmountSum, groupTotalShareSum);
                        double valueBalance = participant.getTotalSpentByCategory(category) - splitValue;
                        if (valueBalance != 0) {
                            SplitResult sr = new SplitResult();
                            sr.setAmount(Math.abs(valueBalance));
                            sr.setDebtType(EDebtType.getDebtType(valueBalance));
                            sr.setSplitGroup(group);
                            sr.setParticipant(participant);
                            sr.setCategory(category);
                            splitResultList.add(sr);
                        }
                    }
                });

        return splitResultList;
    }

    private boolean participantWantsToShareThisCategory(BillCategory category, Participant participant) {
        return participant.getBillCategories().contains(category);
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
