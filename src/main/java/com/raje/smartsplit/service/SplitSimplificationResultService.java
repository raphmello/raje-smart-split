package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.utils.splitresult.DataDebtor;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import com.raje.smartsplit.repository.SplitSimplificationResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SplitSimplificationResultService {

    final private SplitSimplificationResultRepository repository;
    final private UserService userService;
    final private SplitResultService splitResultService;

    public SplitSimplificationResultService(SplitSimplificationResultRepository repository, UserService userService, SplitResultService splitResultService) {
        this.repository = repository;
        this.userService = userService;
        this.splitResultService = splitResultService;
    }

    public Boolean isSimplificationAlreadyCalculated(Long groupId) {
        return repository.existsSplitSimplificationResultBySplitGroup(groupId);
    }

    public void excludeSimplifiedResultsByGroupId(SplitGroup group) {
        List<SplitSimplificationResult> simplificationResults = repository.findAllBySplitGroup(group);
        repository.deleteAll(simplificationResults);
    }

    public List<SplitSimplificationResult> simplifySplitResult(SplitGroup group) {
        List<SplitSimplificationResult> simplifiedResult = new ArrayList<>();
        List<DataDebtor> biggestDebtors = splitResultService.findAllParticipantsOrderByBiggestDebtorsAsc(group.getId());
        int numberParticipantsInGroup = biggestDebtors.size();
        for (int i = 0; i < numberParticipantsInGroup; i++) {
            DataDebtor biggestDebtor = biggestDebtors.get(numberParticipantsInGroup - 1 - i);
            Double amountBiggestDebtor = biggestDebtor.getAmountSum();
            if (amountBiggestDebtor > 0) {
                for (int ii = 0; ii < numberParticipantsInGroup && amountBiggestDebtor > 0; ii++) {
                    DataDebtor smallestDebtor = biggestDebtors.get(ii);

                    if (smallestDebtor.getUsername().equalsIgnoreCase(biggestDebtor.getUsername()))
                        continue;

                    Double amountSmallestDebtor = smallestDebtor.getAmountSum();
                    Double valorParaPagamento = 0.;
                    if (amountSmallestDebtor < 0 && amountBiggestDebtor >= Math.abs(amountSmallestDebtor)) {
                        valorParaPagamento = amountSmallestDebtor;
                        amountBiggestDebtor += valorParaPagamento;
                        biggestDebtor.setAmountSum(biggestDebtor.getAmountSum() + valorParaPagamento);
                        smallestDebtor.setAmountSum(smallestDebtor.getAmountSum() - amountSmallestDebtor);
                    } else if (amountSmallestDebtor < 0 && amountBiggestDebtor < Math.abs(amountSmallestDebtor)) {
                        valorParaPagamento = amountBiggestDebtor;
                        amountBiggestDebtor -= valorParaPagamento;
                        biggestDebtor.setAmountSum(biggestDebtor.getAmountSum() - valorParaPagamento);
                        smallestDebtor.setAmountSum(smallestDebtor.getAmountSum() + valorParaPagamento);
                    }
                    SplitSimplificationResult result = new SplitSimplificationResult();
                    result.setSplitGroup(group);
                    result.setDebtor(userService.getUserByUsername(biggestDebtor.getUsername()));
                    result.setCreditor(userService.getUserByUsername(smallestDebtor.getUsername()));
                    result.setAmount(Math.abs(valorParaPagamento));
                    simplifiedResult.add(result);
                }
            }
        }
        repository.saveAll(simplifiedResult);
        return simplifiedResult;
    }
}
