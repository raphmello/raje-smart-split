package com.raje.smartsplit.controller;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.request.CreateBillRequest;
import com.raje.smartsplit.dto.request.CreateSplitGroupRequest;
import com.raje.smartsplit.dto.response.SplitGroupResponse;
import com.raje.smartsplit.dto.response.SplitGroupSimpleResponse;
import com.raje.smartsplit.entity.SplitGroup;
import com.raje.smartsplit.service.GroupManagementService;
import com.raje.smartsplit.service.SplitGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Group Management")
public class GroupManagementController {

    private final GroupManagementService groupManagementService;
    private final SplitGroupService groupService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GroupManagementController(GroupManagementService groupManagementService, SplitGroupService groupService, JwtUtils jwtUtils) {
        this.groupManagementService = groupManagementService;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    @Operation(summary = "Retrieve all groups (only id and name, not bills and participants) [ONLY ADMIN]")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SplitGroupSimpleResponse>> getAllGroups() {
        List<SplitGroup> entityList = groupService.findAll();
        List<SplitGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    @Operation(summary = "Retrieve all groups from current user (only id and name, not bills and participants)")
    public ResponseEntity<List<SplitGroupSimpleResponse>> getAllGroupsFromCurrentUser() {
        List<SplitGroup> entityList = groupService.findAllGroupsByUsername();
        List<SplitGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/{id}/currentUser")
    @Operation(summary = "Retrieve a group by id that current user is included (includes all bills and participants)")
    public ResponseEntity<SplitGroupResponse> getGroup(@PathVariable("id") Long groupId) {
        Long userId = jwtUtils.getUserFromContext().getId();
        SplitGroupResponse response = groupService.getGroupResponseByIdAndCurrentUser(groupId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/currentUser")
    @Operation(summary = "Create a group and includes the creator as a participant")
    public ResponseEntity<SplitGroupResponse> createGroup(@RequestBody @Valid CreateSplitGroupRequest request) {
        SplitGroup group = groupService.createGroup(request);
        return new ResponseEntity<>(new SplitGroupResponse(group), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/currentUser/enter")
    @Operation(summary = "Enter the group{id}")
    public ResponseEntity<SplitGroupResponse> enterTheGroupById(@PathVariable(value = "id") Long groupId) {
        SplitGroupResponse groupUpdated = groupManagementService.participateInTheGroup(groupId);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/currentUser/exit")
    @Operation(summary = "Remove currentUser from the group{id} and delete all bills related to the user removed")
    public ResponseEntity<SplitGroupResponse> exitTheGroupById(@PathVariable(value = "id") Long groupId) {
        groupManagementService.exitTheGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/currentUser/bill")
    @Operation(summary = "Add new bill to group{id}")
    public ResponseEntity<SplitGroupResponse> addBillToTheGroupId(@PathVariable(value = "id") Long groupId,
                                                                  @RequestBody @Valid CreateBillRequest billRequest) {
        SplitGroupResponse groupUpdated = groupManagementService.addBillToParticipant(groupId, billRequest);
        return new ResponseEntity<>(groupUpdated, HttpStatus.CREATED);
    }
}
