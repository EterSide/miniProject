package com.example.company.service;

import com.example.company.domain.Annual;
import com.example.company.domain.AttendanceHistory;
import com.example.company.domain.Member;
import com.example.company.dto.request.AnnualUseRequest;
import com.example.company.dto.request.AttendanceHistoryCheckRequest;
import com.example.company.dto.request.AttendanceHistoryRequest;
import com.example.company.dto.request.MemberAddRequest;
import com.example.company.dto.response.AttendanceHistoryResponse;
import com.example.company.dto.response.DetailDto;
import com.example.company.dto.response.MemberResponse;
import com.example.company.repository.AnnualRepository;
import com.example.company.repository.AttendanceHistoryRepository;
import com.example.company.repository.MemberRepository;
import com.example.company.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final AttendanceHistoryRepository attendanceHistoryRepository;
    private final AnnualRepository annualRepository;

    public MemberService(
            MemberRepository memberRepository,
            TeamRepository teamRepository,
            AttendanceHistoryRepository attendanceHistoryRepository,
            AnnualRepository annualRepository
    ) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.attendanceHistoryRepository = attendanceHistoryRepository;
        this.annualRepository = annualRepository;
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream().map(
                member -> new MemberResponse(
                        member.getName(),
                        member.getTeam().getName(),
                        member.getRole(),
                        member.getWorkStartDate(),
                        member.getBirthday()
                )
        ).collect(Collectors.toList());
    }

    @Transactional
    public void addMember(MemberAddRequest request) {
        Member member = memberRepository.save(new Member(request.getName(),
                teamRepository.findByName(request.getTeamName()),
                request.getRole(),
                request.getBirthday()));

        System.out.println("startDate = getYear" + member.getWorkStartDate().getYear());
        System.out.println("DateNow = getYear" + LocalDate.now().getYear());

        if (member.getWorkStartDate().getYear() == LocalDate.now().getYear()) {
            annualRepository.save(new Annual(member, 11));
        } else {
            annualRepository.save(new Annual(member, 15));
        }



    }

    public void startAttendanceHistory(AttendanceHistoryRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(IllegalArgumentException::new);

        attendanceHistoryRepository.save(new AttendanceHistory(member));
    }
    @Transactional
    public void endAttendanceHistory(AttendanceHistoryRequest request) {
        // 1. 지정해줄게 필요한듯.. localdate today를 추가했음
        // 2. today를 넣은 AttendanceHistory를 가져와서 수정해야한다
        Member a = memberRepository.findById(request.getMemberId()).orElseThrow(IllegalArgumentException::new);
        AttendanceHistory today = attendanceHistoryRepository.findByTodayAndMember(LocalDate.now(),a );
        System.out.println(today);
        today.setEndTime(LocalDateTime.now());


    }

    public AttendanceHistoryResponse getMemberAllAttendanceHistoryResponses(AttendanceHistoryCheckRequest request) {

        // id / 요청년월로
        Member b = memberRepository.findById(request.getMemberId()).orElseThrow(IllegalArgumentException::new);
        YearMonth month = YearMonth.of(Integer.parseInt(request.getYearMonth().split("-")[0]),Integer.parseInt(request.getYearMonth().split("-")[1]));
        List<AttendanceHistory> attendanceHistories = attendanceHistoryRepository.findAllWithinDateRange(b, month.atDay(1),month.atEndOfMonth());
        long sum = 0;
        List<DetailDto> detailDtoList = new ArrayList<>();
        for(int i=0;i<attendanceHistories.size();i++){
            boolean usingDayoff = false;
            if (attendanceHistories.get(i).getWorkingMinutes() == 0) {
                usingDayoff = true;
            }
            DetailDto detailDto = new DetailDto(attendanceHistories.get(i).getToday(),attendanceHistories.get(i).getWorkingMinutes(), usingDayoff);

            detailDtoList.add(detailDto);
            sum += attendanceHistories.get(i).getWorkingMinutes();
        }
        AttendanceHistoryResponse attendanceHistoryResponse = new AttendanceHistoryResponse(detailDtoList,sum);

        return attendanceHistoryResponse;
    }
    @Transactional
    public void annualUse(AnnualUseRequest request) {

        int period = request.getEndDay().getDayOfMonth() - request.getStartDay().getDayOfMonth();

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(IllegalArgumentException::new);
        int rule = member.getTeam().getRule();
        System.out.println("period의 길이는" + period);
        System.out.println("rule의 길이는 " + rule);

        List<Annual> annuals = annualRepository.findByMember(member);



        System.out.println("annuals의 사이즈" + annuals.size());

        int abc = annuals.get(annuals.size()-1).getAnnualCount() - period;
        if (annuals.get(annuals.size()-1).getAnnualCount() < period) {
            throw new IllegalArgumentException();
        } else if (annuals.get(annuals.size()-1).getAnnualCount() >= period) {
            for (int i = 0; i <= period; i++) {
                AttendanceHistory attendance = attendanceHistoryRepository.save(new AttendanceHistory(member, request.getStartDay().plusDays(i), LocalDateTime.of(request.getStartDay().getYear(), request.getStartDay().getMonth(), request.getStartDay().getDayOfMonth(), 0, 0), LocalDateTime.of(request.getStartDay().getYear(), request.getStartDay().getMonth(), request.getStartDay().getDayOfMonth(), 0, 0)));
                attendance.setToday(request.getStartDay().plusDays(i));
                attendance.setStartTime(LocalDateTime.of(request.getStartDay().getYear(), request.getStartDay().getMonth(), request.getStartDay().getDayOfMonth(), 0, 0));

            }
            annuals.get(annuals.size()-1).setAnnualCount(abc-1);
        }

    }

    public int getAnnual(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        List<Annual> annuals = annualRepository.findByMember(member);

        return annuals.get(annuals.size()-1).getAnnualCount();

    }


}
