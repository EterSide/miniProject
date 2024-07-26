package com.example.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DetailDto {

    private LocalDate date;
    private long workingMinutes;
    private boolean usingDayoff;

}
