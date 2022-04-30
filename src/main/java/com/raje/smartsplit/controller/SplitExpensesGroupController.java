package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.dto.response.SplitExpensesGroupSimpleResponse;
import com.raje.smartsplit.entity.SplitExpensesGroup;
import com.raje.smartsplit.service.SplitExpensesGroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/group")
public class SplitExpensesGroupController {

    private final SplitExpensesGroupService service;

    @Autowired
    public SplitExpensesGroupController(SplitExpensesGroupService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retreive all groups (only id and name, not bills and participants)")
    public ResponseEntity<List<SplitExpensesGroupSimpleResponse>> getAllGroups() {
        List<SplitExpensesGroup> entityList = service.findAll();
        List<SplitExpensesGroupSimpleResponse> responseList = new ArrayList<>();
        entityList.forEach(group -> responseList.add(new SplitExpensesGroupSimpleResponse(group)));
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a group by id (includes all bills and participants)")
    public ResponseEntity<SplitExpensesGroupResponse> getGroup(@PathVariable("id") Long groupId) {
        SplitExpensesGroupResponse response = service.getGroupById(groupId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Create a group and includes the creator as a participant")
    public ResponseEntity<SplitExpensesGroupResponse> createGroup(@RequestBody CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroup group = service.createGroup(request);
        return new ResponseEntity<>(new SplitExpensesGroupResponse(group), HttpStatus.CREATED);
    }
}
