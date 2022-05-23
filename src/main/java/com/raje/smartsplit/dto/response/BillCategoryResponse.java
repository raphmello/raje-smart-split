package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.BillCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BillCategoryResponse {
    private Long id;
    private String category;
    private String description;

    public BillCategoryResponse(BillCategory billCategory) {
        this.id = billCategory.getId();
        this.category = billCategory.getCategory();
        this.description = billCategory.getDescription();
    }
}
