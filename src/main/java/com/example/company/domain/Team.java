package com.example.company.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class Team {
    @Id // javax
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment를 사용했고, IDENTITY와 매치되서 사용하는것
    private Long id = null;

    private String name;

    private int rule;

    @OneToMany(mappedBy = "team")
    private List<Member> Members = new ArrayList<>();

    public Team(String name, int rule) {
        this.name = name;
        this.rule = rule;
    }

    public String getManager() {
        return Members.stream()
                .filter(member -> member.getRole().equals("Manager"))
                .findFirst()
                .orElseThrow(IllegalAccessError::new).getName();
    }

    public int getMemberCount() {
        return Members.size();
    }
}
