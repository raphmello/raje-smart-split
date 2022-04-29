package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.AppUser;
import com.raje.smartsplit.entity.Bill;
import com.raje.smartsplit.entity.SplitExpensesGroup;
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
    private List<AppUser> appUsers;

    public SplitExpensesGroupResponse(SplitExpensesGroup entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.bills = entity.getBills();
        this.appUsers = entity.getAppUsers();
    }
}
