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

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all categories")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategories() {
        Set<BillCategory> categories = categoryService.findAll();
        Set<BillCategoryResponse> responseList = categories.stream()
                .map(BillCategoryResponse::new)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("group/{id}")
    @Operation(summary = "Retrieve all categories from bills by group{id}")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategoriesByGroupId(@PathVariable("id") Long groupId) {
        Set<BillCategory> categories = categoryService.findAllByGroupId(groupId);
        Set<BillCategoryResponse> responseList = categories.stream()
                .map(BillCategoryResponse::new)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
