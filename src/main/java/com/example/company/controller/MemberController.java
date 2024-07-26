package com.example.company.controller;


import com.example.company.dto.request.AnnualUseRequest;
import com.example.company.dto.request.AttendanceHistoryCheckRequest;
import com.example.company.dto.request.AttendanceHistoryRequest;
import com.example.company.dto.request.MemberAddRequest;
import com.example.company.dto.response.AttendanceHistoryResponse;
import com.example.company.dto.response.MemberResponse;
import com.example.company.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member")
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();

    }

    @PostMapping("/member")
    public void addMember(@RequestBody MemberAddRequest request) {
        memberService.addMember(request);
    }

    @PostMapping("/member/attendanceHistory")
    public void startAttendanceHistory(@RequestBody AttendanceHistoryRequest request) {
        System.out.println(request.getMemberId());
        memberService.startAttendanceHistory(request);
    }

    @PutMapping("/member/attendanceHistory")
    public void endAttendanceHistory(@RequestBody AttendanceHistoryRequest request) {
        System.out.println("put");
        memberService.endAttendanceHistory(request);
    }

    @GetMapping("/member/attendanceHistories")
    public AttendanceHistoryResponse getMemberAllAttendanceHistoryResponses(@RequestBody AttendanceHistoryCheckRequest request) {
        return memberService.getMemberAllAttendanceHistoryResponses(request);
    }

    @PostMapping("/member/annual")
    public void annualUse(@RequestBody AnnualUseRequest request) {
        memberService.annualUse(request);
    }

    @GetMapping("/member/annual")
    public int getAnnual(@RequestParam Long memberId) {
        return memberService.getAnnual(memberId);
    }


}
