package com.example.company.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class AllMemberOverWorkResponse {

    private List<OverTimeDto> allOverTime;

    public AllMemberOverWorkResponse(List<OverTimeDto> allOverTime) {
        this.allOverTime = allOverTime;
    }
}
