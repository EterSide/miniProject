package com.example.company.repository;

import com.example.company.domain.Annual;
import com.example.company.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnualRepository extends JpaRepository<Annual, Long> {

    List<Annual> findByMember(Member member);
    Annual findByMemberAndYear(Member member, int year);
}
