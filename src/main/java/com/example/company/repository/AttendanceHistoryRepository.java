package com.example.company.repository;
import com.example.company.domain.AttendanceHistory;
import com.example.company.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistory, Long> {

    AttendanceHistory findByTodayAndMember(LocalDate today, Member member);

    @Query("SELECT e FROM AttendanceHistory e WHERE e.member = :memberId AND e.today BETWEEN :startDate AND :endDate")
    List<AttendanceHistory> findAllWithinDateRange(@Param("memberId") Member member,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

}
