package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.service.GroupManagementService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupManagement")
public class GroupManagementController {

    private final GroupManagementService service;

    @Autowired
    public GroupManagementController(GroupManagementService service) {
        this.service = service;
    }

    @PostMapping("/group/{groupId}/user/{userId}")
    @Operation(summary = "Add a user{userId} to the group{groupId}")
    public ResponseEntity<SplitExpensesGroupResponse> addParticipant(@PathVariable(value = "groupId") Long groupId,
                                                                     @PathVariable(value = "userId") Long userId) {
        SplitExpensesGroupResponse groupUpdated = service.addParticipantToGroup(userId,groupId);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }
}
