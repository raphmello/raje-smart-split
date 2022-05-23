package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.response.SplitSimplificationResponse;
import com.raje.smartsplit.entity.SplitResult;
import com.raje.smartsplit.entity.SplitSimplificationResult;
import com.raje.smartsplit.service.SmartCalculatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calculator")
@Tag(name = "Smart Split Calculator")
public class SmartCalculatorController {

    private final SmartCalculatorService smartCalculatorService;

    public SmartCalculatorController(SmartCalculatorService smartCalculatorService) {
        this.smartCalculatorService = smartCalculatorService;
    }

    @GetMapping("/simplify/group/{id}")
    public ResponseEntity<List<SplitSimplificationResponse>> simplify(@PathVariable("id") Long groupId,
                                                                      Boolean overwrite) {
        List<SplitSimplificationResult> resultList = smartCalculatorService.simplifySplit(groupId, overwrite);
        List<SplitSimplificationResponse> responseList = new ArrayList<>();
        resultList.forEach(r -> {
            responseList.add(new SplitSimplificationResponse(r));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
