package com.example.company.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id // javax
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment를 사용했고, IDENTITY와 매치되서 사용하는것
    private Long id = null;

    private String name;

    @ManyToOne
    private Team team;

    private String role;

    @CreationTimestamp
    private LocalDate workStartDate;
    //private LocalDate workStartDate = LocalDate.of(2023,1,1);

    private LocalDate birthday;

    @OneToMany(mappedBy = "member")
    private List<AttendanceHistory> attendanceHistories = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Annual> annuals = new ArrayList<>();

    public Member(String name, Team team, String role, LocalDate birthday) {
        this.name = name;
        this.team = team;
        this.role = role;
        this.birthday = birthday;
    }
}
