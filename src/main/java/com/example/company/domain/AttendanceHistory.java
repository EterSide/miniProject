package com.example.company.domain;
import java.time.Duration;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class AttendanceHistory {

    @Id // javax
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;
    @CreationTimestamp()
    private LocalDate today;

    @CreationTimestamp()
    private LocalDateTime startTime;

    private LocalDateTime endTime;


    public long getWorkingMinutes(){
        LocalDateTime startTime = LocalDateTime.of(getStartTime().getYear(), getStartTime().getMonth(), getStartTime().getDayOfMonth(), getStartTime().getHour(), getStartTime().getMinute()); // 예시 시작 시간
        LocalDateTime endTime = LocalDateTime.of(getEndTime().getYear(), getEndTime().getMonth(), getEndTime().getDayOfMonth(), getEndTime().getHour(), getEndTime().getMinute());
        Duration duration = Duration.between(startTime, endTime);

        // 분 단위로 변환
        long minutes = duration.toMinutes();
        return minutes;
    }

    public AttendanceHistory(Member member) {
        this.member = member;
    }

    public AttendanceHistory(Member member, LocalDate today, LocalDateTime startTime, LocalDateTime endTime) {
        this.member = member;
        this.today = today;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
