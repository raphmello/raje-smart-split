package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import com.raje.smartsplit.service.SplitExpensesGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/group")
public class SplitExpensesGroupController {

    private final SplitExpensesGroupService service;

    @Autowired
    public SplitExpensesGroupController(SplitExpensesGroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<SplitExpensesGroupResponse> getGroup(@RequestParam Long groupId) {
        SplitExpensesGroupResponse response = service.getGroupById(groupId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<SplitExpensesGroupResponse> createGroup(@RequestBody CreateSplitExpensesGroupRequest request) {
        SplitExpensesGroupResponse group = service.createGroup(request);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }
}
