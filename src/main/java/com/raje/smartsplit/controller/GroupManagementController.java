package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.dto.response.SplitExpensesGroupSimpleResponse;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.service.GroupManagementService;
import com.raje.smartsplit.service.SplitExpensesGroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/group")
public class GroupManagementController {

    private final GroupManagementService groupManagementService;

    private final SplitExpensesGroupService groupService;

    @Autowired
    public GroupManagementController(GroupManagementService groupManagementService, SplitExpensesGroupService groupService) {
        this.groupManagementService = groupManagementService;
        this.groupService = groupService;
    }

    @GetMapping
    @Operation(summary = "Retreive all groups (only id and name, not bills and participants)")
    public ResponseEntity<List<SplitExpensesGroupSimpleResponse>> getAllGroups() {
        List<SplitExpensesGroup> entityList = groupService.findAll();
        List<SplitExpensesGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitExpensesGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a group by id (includes all bills and participants)")
    public ResponseEntity<SplitExpensesGroupResponse> getGroup(@PathVariable("id") Long groupId) {
        SplitExpensesGroupResponse response = groupService.getGroupById(groupId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Create a group and includes the creator as a participant")
    public ResponseEntity<SplitExpensesGroupResponse> createGroup(@RequestBody @Valid CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup group = groupService.createGroup(request);
        return new ResponseEntity<>(new SplitExpensesGroupResponse(group), HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/user/{userId}")
    @Operation(summary = "Add a user{userId} to the group{groupId}")
    public ResponseEntity<SplitExpensesGroupResponse> addParticipantToGroup(@PathVariable(value = "groupId") Long groupId,
                                                                     @PathVariable(value = "userId") Long userId) {
        SplitExpensesGroupResponse groupUpdated = groupManagementService.addParticipantToGroup(userId,groupId);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }
}
