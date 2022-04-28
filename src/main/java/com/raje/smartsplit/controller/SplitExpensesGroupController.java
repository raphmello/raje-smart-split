package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.request.CreateSplitExpensesGroupRequest;
import com.raje.smartsplit.dto.response.SplitExpensesGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/group")
public class SplitExpensesGroupController {

    @GetMapping
    public ResponseEntity<SplitExpensesGroupResponse> getGroup(@RequestParam Long groupId) {
        return new ResponseEntity<>(new SplitExpensesGroupResponse(groupId, "Title", new ArrayList<>()), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<SplitExpensesGroupResponse> createGroup(@RequestBody CreateSplitExpensesGroupRequest request) {
        return new ResponseEntity<>(new SplitExpensesGroupResponse(1L, request.getTitle(), new ArrayList<>()), HttpStatus.CREATED);
    }
}
