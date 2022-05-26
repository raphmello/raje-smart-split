package com.raje.smartsplit.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SplitGroupSplitResultResponse {
    private SplitGroupResponse group;
    private List<SplitResultResponse> splitResults;

    public SplitGroupSplitResultResponse(SplitGroupResponse group, List<SplitResultResponse> result) {
        this.group = group;
        this.splitResults = result;
    }
}
