package com.raje.smartsplit.service;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.repository.SplitExpensesGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SplitExpensesGroupService {

    private final SplitExpensesGroupRepository repository;

    @Autowired
    public SplitExpensesGroupService(SplitExpensesGroupRepository repository) {
        this.repository = repository;
    }

    public SplitExpensesGroupResponse createGroup(CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup newGroup = new SplitExpensesGroup();
        newGroup.setTitle(request.getTitle());
        SplitExpensesGroup entity = repository.save(newGroup);
        return new SplitExpensesGroupResponse(entity);
    }

    public SplitExpensesGroupResponse getGroupById(Long groupId) {
        Optional<SplitExpensesGroup> optional = repository.findById(groupId);
        if (optional.isPresent())
            return new SplitExpensesGroupResponse(optional.get());
        throw new RuntimeException("Id not found.");
    }
}
