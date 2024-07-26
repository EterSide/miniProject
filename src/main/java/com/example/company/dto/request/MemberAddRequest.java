package com.example.company.dto.request;

import com.example.company.domain.Team;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
public class MemberAddRequest {

    private String name;

    private String teamName;

    private String role;


    private LocalDate workStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;



}
