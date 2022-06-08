package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SmartCalculatorService {
    private final SplitSimplificationResultService simplificationResultService;
    private final SplitGroupService groupService;

    public SmartCalculatorService(SplitSimplificationResultService simplificationResultService, SplitGroupService groupService) {
        this.simplificationResultService = simplificationResultService;
        this.groupService = groupService;
    }

    @Transactional
    public List<SplitSimplificationResult> simplifySplit(Long groupId, Boolean overwrite) {
        Boolean simplificationAlreadyCalculated = simplificationResultService.isSimplificationAlreadyCalculated(groupId);

        SplitGroup group = groupService.getGroupById(groupId);

        if (shouldOverwriteOldSimplification(overwrite, simplificationAlreadyCalculated)) {
            simplificationResultService.excludeSimplifiedResultsByGroupId(group);
            return calculateSimplifiedResult(group);
        } else if (isFirstSimplification(simplificationAlreadyCalculated)) {
            return calculateSimplifiedResult(group);
        } else {
            throw new RuntimeException("Not able to simplify the split");
        }

    }

    private List<SplitSimplificationResult> calculateSimplifiedResult(SplitGroup group) {
        return simplificationResultService.simplifySplitResult(group);
    }

    private boolean shouldOverwriteOldSimplification(Boolean overwrite, Boolean simplificationAlreadyCalculated) {
        return simplificationAlreadyCalculated && overwrite;
    }

    private boolean isFirstSimplification(Boolean simplificationAlreadyCalculated) {
        return !simplificationAlreadyCalculated;
    }
}
