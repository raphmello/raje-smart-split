package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SplitExpensesGroupResponse {
    private Long id;
    private String title;
    private List<Bill> bills;
}
