package com.raje.smartsplit.controller;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.response.BillCategoryResponse;
import com.raje.smartsplit.dto.response.ParticipantSplitGroupResponse;
import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.service.CategoryService;
import com.raje.smartsplit.service.SplitGroupService;
import com.raje.smartsplit.service.SplitResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final SplitResultService splitResultService;
    private final JwtUtils jwtUtils;

    public CategoryController(CategoryService categoryService, SplitResultService splitResultService, JwtUtils jwtUtils) {
        this.categoryService = categoryService;
        this.splitResultService = splitResultService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    @Operation(summary = "Retrieve all categories")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategories() {
        Set<BillCategory> categories = categoryService.findAll();
        Set<BillCategoryResponse> responseList = new HashSet<>();
        categories.forEach(c -> {
            responseList.add(new BillCategoryResponse(c));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("group/{id}")
    @Operation(summary = "Retrieve all categories from bills by group{id}")
    public ResponseEntity<Set<BillCategoryResponse>> getAllCategoriesByGroupId(@PathVariable("id") Long groupId) {
        Set<BillCategory> categories = categoryService.findAllByGroupId(groupId);
        Set<BillCategoryResponse> responseList = new HashSet<>();
        categories.forEach(c -> {
            responseList.add(new BillCategoryResponse(c));
        });
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PostMapping("group/{id}")
    @Operation(summary = "Update categories for currentUser by group{id}", description = "Include categories that current user wants to split inside the group")
    public ResponseEntity<ParticipantSplitGroupResponse> updateCategoriesForCurrentUserByGroupId(@PathVariable("id") Long groupId,
                                                                                                 @RequestBody List<Long> categoriesId) {
        User currentUser = jwtUtils.getUserFromContext();
        ParticipantSplitGroupResponse participantSplitGroupResponse = splitResultService.updateCategories(groupId, categoriesId, currentUser);
        return new ResponseEntity<>(participantSplitGroupResponse, HttpStatus.OK);
    }

}
