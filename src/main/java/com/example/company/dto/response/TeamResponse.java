package com.example.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamResponse {

    private String name;
    private String managerName;
    private int memberCount;

}
