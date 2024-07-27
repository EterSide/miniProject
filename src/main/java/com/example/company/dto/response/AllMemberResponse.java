package com.example.company.dto.response;

import lombok.Getter;

import java.util.List;
@Getter
public class AllMemberResponse {

    OverTimeDto allOverTime;

    public AllMemberResponse(OverTimeDto allOverTime) {
        this.allOverTime = allOverTime;
    }
}
