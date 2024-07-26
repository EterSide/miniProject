package com.example.company.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @NoArgsConstructor
@Getter @Setter
public class Annual {

    @Id // javax
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment를 사용했고, IDENTITY와 매치되서 사용하는것
    private Long id = null;

    @ManyToOne
    private Member member;

    private int annualCount;

    public Annual(Member member, int annualCount) {
        this.member = member;
        this.annualCount = annualCount;
    }
}
