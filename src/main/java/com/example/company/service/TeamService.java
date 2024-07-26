package com.example.company.service;

import com.example.company.domain.Team;
import com.example.company.dto.request.TeamAddRequest;
import com.example.company.dto.response.TeamResponse;
import com.example.company.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void addTeam(TeamAddRequest request) {
        teamRepository.save(new Team(request.getName(), 1));
    }

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream().map(
                team -> new TeamResponse(
                        team.getName(),
                        team.getManager(),
                        team.getMemberCount()
                )
        ).collect(Collectors.toList());

    }

}
