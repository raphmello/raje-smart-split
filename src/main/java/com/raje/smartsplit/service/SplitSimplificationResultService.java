package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import com.raje.smartsplit.repository.SplitResultRepository;
import com.raje.smartsplit.repository.SplitSimplificationResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SplitSimplificationResultService {

    SplitSimplificationResultRepository repository;

    public Boolean isSimplificationAlreadyCalculated(Long groupId) {
        return repository.existsSplitSimplificationResultBySplitGroup(groupId);
    }

    public Boolean excludeSimplifiedResultsByGroupId(Long groupId) {
        return repository.deleteAllBySplitGroup(groupId);
    }

    public List<SplitSimplificationResult> simplifySplitResult(Long groupId) {
        //TODO
        return new ArrayList<>();
    }
}
