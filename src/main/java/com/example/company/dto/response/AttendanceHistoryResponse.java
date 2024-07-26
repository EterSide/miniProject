package com.example.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class AttendanceHistoryResponse {

    private List<DetailDto> detail;
    private long sum;

}
