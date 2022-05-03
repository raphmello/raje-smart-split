package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateBillRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "Retreive all groups (only id and name, not bills and participants) [ONLY ADMIN]")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SplitExpensesGroupSimpleResponse>> getAllGroups() {
        List<SplitExpensesGroup> entityList = groupService.findAll();
        List<SplitExpensesGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitExpensesGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    @Operation(summary = "Retreive all groups from current user (only id and name, not bills and participants)")
    public ResponseEntity<List<SplitExpensesGroupSimpleResponse>> getAllGroupsFromCurrentUser() {
        List<SplitExpensesGroup> entityList = groupService.findAllGroupsByUsername();
        List<SplitExpensesGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitExpensesGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/{id}/currentUser")
    @Operation(summary = "Retrieve a group by id that current user is included (includes all bills and participants)")
    public ResponseEntity<SplitExpensesGroupResponse> getGroup(@PathVariable("id") Long groupId) {
        SplitExpensesGroupResponse response = groupService.getGroupByIdAndCurrentUser(groupId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/currentUser")
    @Operation(summary = "Create a group and includes the creator as a participant")
    public ResponseEntity<SplitExpensesGroupResponse> createGroup(@RequestBody @Valid CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup group = groupService.createGroup(request);
        return new ResponseEntity<>(new SplitExpensesGroupResponse(group), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/currentUser/enter")
    @Operation(summary = "Enter the group{id}")
    public ResponseEntity<SplitExpensesGroupResponse> enterTheGroupById(@PathVariable(value = "id") Long groupId) {
        SplitExpensesGroupResponse groupUpdated = groupManagementService.participateInTheGroup(groupId);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/currentUser/addBill")
    @Operation(summary = "Add new bill to group{id}")
    public ResponseEntity<SplitExpensesGroupResponse> addBillToTheGroupId(@PathVariable(value = "id") Long groupId,
                                                                          @RequestBody @Valid CreateBillRequest billRequest) {
        SplitExpensesGroupResponse groupUpdated = groupManagementService.addBillToParticipant(groupId, billRequest);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }
}
