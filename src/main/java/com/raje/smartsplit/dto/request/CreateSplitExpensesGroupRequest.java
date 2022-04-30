package com.raje.smartsplit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSplitExpensesGroupRequest {
    @NotNull
    private String title;
    @NotNull
    private Long userId;
}
