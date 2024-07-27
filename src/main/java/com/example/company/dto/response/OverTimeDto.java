package com.example.company.dto.response;

import lombok.Getter;

@Getter
public class OverTimeDto {

    private Long id;
    private String name;
    private long overTimeMinutes;

    public OverTimeDto(Long id, String name, long overTimeMinutes) {
        this.id = id;
        this.name = name;
        this.overTimeMinutes = overTimeMinutes;
    }
}
