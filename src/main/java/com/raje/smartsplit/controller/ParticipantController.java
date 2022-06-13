package com.raje.smartsplit.controller;

import com.raje.smartsplit.config.SecurityConfig.JwtUtils;
import com.raje.smartsplit.dto.response.ParticipantResponse;
import com.raje.smartsplit.dto.response.ParticipantSplitGroupResponse;
import com.raje.smartsplit.entity.Participant;
import com.raje.smartsplit.entity.User;
import com.raje.smartsplit.service.CategoryService;
import com.raje.smartsplit.service.ParticipantService;
import com.raje.smartsplit.service.SplitResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/participant")
@Tag(name = "Participant Management")
public class ParticipantController {

    private final JwtUtils jwtUtils;
    private final SplitResultService splitResultService;
    private final ParticipantService participantService;

    public ParticipantController(JwtUtils jwtUtils, SplitResultService splitResultService, ParticipantService participantService) {
        this.jwtUtils = jwtUtils;
        this.splitResultService = splitResultService;
        this.participantService = participantService;
    }

    @GetMapping("category/group/{id}")
    @Operation(summary = "Retrieve categories for currentUser by group{id}", description = "Retrieve categories that current user wants to split inside the group")
    public ResponseEntity<ParticipantResponse> getCategoriesForCurrentUserByGroupId(@PathVariable("id") Long groupId) {
        User currentUser = jwtUtils.getUserFromContext();
        Participant participant = participantService.findByGroupIdAndUserId(groupId, currentUser.getId());
        return new ResponseEntity<>(new ParticipantResponse(participant), HttpStatus.OK);
    }

    @PostMapping("category/group/{id}")
    @Operation(summary = "Update categories for currentUser by group{id}", description = "Include categories that current user wants to split inside the group")
    public ResponseEntity<ParticipantSplitGroupResponse> updateCategoriesForCurrentUserByGroupId(@PathVariable("id") Long groupId,
                                                                                                 @RequestBody List<Long> categoriesId) {
        User currentUser = jwtUtils.getUserFromContext();
        ParticipantSplitGroupResponse participantSplitGroupResponse = splitResultService.updateCategories(groupId, categoriesId, currentUser);
        return new ResponseEntity<>(participantSplitGroupResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update split share for participant{id}", description = "Update split share for the participant")
    public ResponseEntity<ParticipantSplitGroupResponse> updateSplitShareForCurrentParticipant(@PathVariable("id") Long participantId,
                                                                                                 @RequestBody Double splitShare) {
        User currentUser = jwtUtils.getUserFromContext();
        ParticipantSplitGroupResponse participantSplitGroupResponse = participantService.updateSplitShare(participantId, splitShare, currentUser);
        return new ResponseEntity<>(participantSplitGroupResponse, HttpStatus.OK);
    }

}
