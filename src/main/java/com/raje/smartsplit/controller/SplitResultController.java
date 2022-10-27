package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.response.SplitResultResponse;
import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.service.SplitResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/splitResult")
@Tag(name = "Split Result")
public class SplitResultController {

    private final SplitResultService splitResultService;

    public SplitResultController(SplitResultService splitResultService) {
        this.splitResultService = splitResultService;
    }

    @GetMapping("/update/group/{id}")
    public ResponseEntity<List<SplitResultResponse>> updateSplitResult(@PathVariable("id") Long groupId) {
        List<SplitResult> resultList = splitResultService.updateSplitResult(groupId);
        List<SplitResultResponse> responseList = resultList.stream()
                .map(SplitResultResponse::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
