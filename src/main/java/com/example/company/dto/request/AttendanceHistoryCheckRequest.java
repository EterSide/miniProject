package com.example.company.dto.request;

import lombok.Getter;

@Getter
public class AttendanceHistoryCheckRequest {

    private long memberId;
    private String yearMonth;
}
