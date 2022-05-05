package com.raje.smartsplit.dto.response;

import com.raje.smartsplit.entity.SplitGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SplitGroupSimpleResponse {
    private Long id;
    private String title;

    public SplitGroupSimpleResponse(SplitGroup entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
