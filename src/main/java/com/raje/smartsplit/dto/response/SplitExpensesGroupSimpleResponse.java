package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.SplitExpensesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SplitExpensesGroupSimpleResponse {
    private Long id;
    private String title;

    public SplitExpensesGroupSimpleResponse(SplitExpensesGroup entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
