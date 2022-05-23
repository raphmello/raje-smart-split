package com.raje.smartsplit.controller;

import com.raje.smartsplit.dto.response.BillCategoryResponse;
import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Retreive all categories")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategories() {
        Set<BillCategory> categories = service.findAll();
        Set<BillCategoryResponse> responseList = new HashSet<>();
        categories.forEach(c -> {
            responseList.add(new BillCategoryResponse(c));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("group/{id}")
    @Operation(summary = "Retreive all categories for specific group{id}")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategoriesByGroupId(@PathVariable("id") Long groupId) {
        Set<BillCategory> categories = service.findAllByGroupId(groupId);
        Set<BillCategoryResponse> responseList = new HashSet<>();
        categories.forEach(c -> {
            responseList.add(new BillCategoryResponse(c));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

}
