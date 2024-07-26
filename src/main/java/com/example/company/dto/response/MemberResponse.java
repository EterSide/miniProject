package com.example.company.dto.response;

import com.example.company.domain.Team;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponse {

    private String name;
    private String teamName;

    private String role;

    private LocalDate workStartDate;

    private LocalDate birthday;

    public MemberResponse(String name, String teamName, String role, LocalDate workStartDate, LocalDate birthday) {
        this.name = name;
        this.teamName = teamName;
        this.role = role;
        this.workStartDate = workStartDate;
        this.birthday = birthday;
    }
}
