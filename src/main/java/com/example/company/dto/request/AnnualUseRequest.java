package com.example.company.dto.request;

import lombok.Getter;

import java.time.LocalDate;
@Getter
public class AnnualUseRequest {

    private Long memberId;

    private int year;

    private LocalDate useDay;
    //private LocalDate endDay;

}
