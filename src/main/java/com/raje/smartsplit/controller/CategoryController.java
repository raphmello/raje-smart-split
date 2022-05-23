package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.response.BillCategoryResponse;
import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retreive all categories")
    public ResponseEntity<List<BillCategoryResponse>> getAllGroups() {
        List<BillCategory> categories = service.findAll();
        List<BillCategoryResponse> responseList = new ArrayList<>();
        categories.forEach(c -> {
            responseList.add(new BillCategoryResponse(c));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

}
