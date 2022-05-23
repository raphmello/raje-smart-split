package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SmartCalculatorService {
    private final SplitSimplificationResultService simplificationResultService;

    public SmartCalculatorService(SplitSimplificationResultService simplificationResultService) {
        this.simplificationResultService = simplificationResultService;
    }

    @Transactional
    public List<SplitSimplificationResult> simplifySplit(Long groupId, Boolean overwrite) {
        Boolean simplificationAlreadyCalculated = simplificationResultService.isSimplificationAlreadyCalculated(groupId);

        if (shouldOverwriteOldSimplification(overwrite, simplificationAlreadyCalculated)) {
            simplificationResultService.excludeSimplifiedResultsByGroupId(groupId);
            return calculateSimplifiedResult(groupId);
        } else if (isFirstSimplification(simplificationAlreadyCalculated)) {
            return calculateSimplifiedResult(groupId);
        } else {
            throw new RuntimeException("Not able to simplify the split");
        }

    }

    private List<SplitSimplificationResult> calculateSimplifiedResult(Long groupId) {
        return simplificationResultService.simplifySplitResult(groupId);
    }

    private boolean shouldOverwriteOldSimplification(Boolean overwrite, Boolean simplificationAlreadyCalculated) {
        return simplificationAlreadyCalculated && overwrite;
    }

    private boolean isFirstSimplification(Boolean simplificationAlreadyCalculated) {
        return !simplificationAlreadyCalculated;
    }
}
