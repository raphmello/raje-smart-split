package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.repository.BillRepository;
import com.raje.smartsplit.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    private final BillRepository repository;

    @Autowired
    public BillService(BillRepository repository) {
        this.repository = repository;
    }

    public List<Bill> findBillsByGroupId(Long groupId) {
        return repository.findBySplitExpensesGroup(groupId);
    }
}
