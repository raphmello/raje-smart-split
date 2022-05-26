package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.enums.EDebtType;
import com.raje.smartsplit.repository.SplitResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SplitResultService {

    private final SplitResultRepository repository;
    private final SplitGroupService groupService;

    public SplitResultService(SplitResultRepository repository, SplitGroupService groupService) {
        this.repository = repository;
        this.groupService = groupService;
    }

    private List<SplitResult> calculatesDebtorsAndCreditors(SplitGroup group) {
        List<Double> amountList = new ArrayList<>();
        List<Double> shares = new ArrayList<>();
        group.getParticipants().forEach(participant -> {
            Double share = participant.getSplitShare();
            shares.add(share);
            for (Bill bill : participant.getBills()) {
                amountList.add(bill.getAmount());
            }
        });

        double amountSum = amountList.stream().mapToDouble(Double::doubleValue).sum();
        double shareSum = shares.stream().mapToDouble(Double::doubleValue).sum();

        List<SplitResult> splitResultList = new ArrayList<>();

        group.getParticipants()
                .forEach(participant -> {
                    Double value = splitValue(participant, amountSum, shareSum);
                    double valueBalance = participant.getTotalSpent() - value;
                    if (valueBalance != 0) {
                        SplitResult sr = new SplitResult();
                        sr.setAmount(Math.abs(valueBalance));
                        sr.setDebtType(EDebtType.getDebtType(valueBalance));
                        sr.setSplitGroup(group);
                        sr.setParticipant(participant);
                        sr.setCategory(participant.getBillCategories().stream().findFirst().get());
                        splitResultList.add(sr);
                    }
                });

        saveMultipleSplitResult(group, splitResultList);

        return splitResultList;
    }

    private void saveMultipleSplitResult(SplitGroup group, List<SplitResult> splitResultList) {
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

    private Double splitValue(Participant participant, double amountSum, double shareSum) {
        Double userShare = participant.getSplitShare();
        return (userShare/shareSum) * amountSum;
    }

    public List<SplitResult> updateSplitResult(SplitGroup group) {
        return calculatesDebtorsAndCreditors(group);
    }

    public List<SplitResult> updateSplitResult(Long groupId) {
        SplitGroup group = groupService.getGroupById(groupId);
        return calculatesDebtorsAndCreditors(group);
    }
}
