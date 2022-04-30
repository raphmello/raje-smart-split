package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.AppUserRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.service.GroupManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupManagement")
public class GroupManagementController {

    private final GroupManagementService service;

    @Autowired
    public GroupManagementController(GroupManagementService service) {
        this.service = service;
    }

    @PutMapping("/group/{id}")
    public ResponseEntity<SplitExpensesGroupResponse> addParticipant(@RequestBody AppUserRequest user,
                                                                     @PathVariable(value = "id") Long groupId) {
        SplitExpensesGroupResponse groupUpdated = service.addParticipantToGroup(user,groupId);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }
}
