package com.example.company.controller;

import com.example.company.dto.request.TeamAddRequest;
import com.example.company.dto.response.TeamResponse;
import com.example.company.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class
TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/teams")
    public void addTeam(@RequestBody TeamAddRequest request) {
        teamService.addTeam(request);
    }
    @GetMapping("/teams")
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }


}
