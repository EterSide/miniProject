package com.example.company.service;

import com.example.company.domain.Annual;
import com.example.company.domain.AttendanceHistory;
import com.example.company.domain.Member;
import com.example.company.dto.request.AnnualUseRequest;
import com.example.company.dto.request.AttendanceHistoryCheckRequest;
import com.example.company.dto.request.AttendanceHistoryRequest;
import com.example.company.dto.request.MemberAddRequest;
import com.example.company.dto.response.*;
import com.example.company.repository.AnnualRepository;
import com.example.company.repository.AttendanceHistoryRepository;
import com.example.company.repository.MemberRepository;
import com.example.company.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
        // 연차 기간 계산
        //int period = request.getEndDay().getDayOfMonth() - request.getStartDay().getDayOfMonth();
        int period = 1;
        // 멤버 정의
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(IllegalArgumentException::new);
        // 팀 연차 사용 규칙
        int rule = member.getTeam().getRule();
        System.out.println("period의 길이는" + period);
        System.out.println("rule의 길이는 " + rule);

        System.out.println("useDay = " + request.getUseDay());
        
        // 연차목록
        //List<Annual> annuals = annualRepository.findByMember(member);
        Annual byIdAndYear = annualRepository.findByMemberAndYear(member, request.getYear());

        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), request.getUseDay());

        System.out.println("days" + daysBetween);

        // 가지고 있는 연차가 지금 쓰려는 기간보다 긴지 확인
        int abc = byIdAndYear.getAnnualCount() - period;


        // 가진 연차보다 오래 썻거나 사용일이 규칙보다 짧으면 오류 발생하게
        if (byIdAndYear.getAnnualCount() < period && daysBetween < rule) {
            throw new IllegalArgumentException();
        } else if (byIdAndYear.getAnnualCount() >= period && daysBetween > rule ) {
            System.out.println("작동되나요?");
            AttendanceHistory attendance = attendanceHistoryRepository.save(new AttendanceHistory(member, null, null, LocalDateTime.of(request.getUseDay().getYear(), request.getUseDay().getMonth(), request.getUseDay().getDayOfMonth(), 0, 0)));
            attendance.setToday(request.getUseDay());
            attendance.setStartTime(LocalDateTime.of(request.getUseDay().getYear(), request.getUseDay().getMonth(), request.getUseDay().getDayOfMonth(), 0, 0));
            byIdAndYear.setAnnualCount(abc); // 사용한 만큼 연차일도 수정
        }

    }

    public int getAnnual(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        List<Annual> annuals = annualRepository.findByMember(member);

        return annuals.get(annuals.size()-1).getAnnualCount();

    }

    public AllMemberResponse getAllOverTime() {
        
        // 1. 모든 멤버를 가져온다
        List<Member> members = memberRepository.findAll();
        // 2. 달을 기반으로 각 멤버의 총 업무시간을 나오게한다



        return null;
    }


}
