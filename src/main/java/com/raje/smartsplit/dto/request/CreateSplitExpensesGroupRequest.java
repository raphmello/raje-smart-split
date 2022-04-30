package com.raje.smartsplit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSplitExpensesGroupRequest {
    @NotBlank
    private String title;
    @NotNull
    @Min(value = 1)
    private Long userId;
}
