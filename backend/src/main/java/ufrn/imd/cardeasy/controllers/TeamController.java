package ufrn.imd.cardeasy.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ufrn.imd.cardeasy.dtos.TeamDTO;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.services.TeamService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> getTeams() {
        return ResponseEntity.ok(teamService.findTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable UUID id) {
        return ResponseEntity.ok(teamService.findTeam(id));
    }

    @PostMapping
    public ResponseEntity<Team> addTeam(@RequestBody TeamDTO teamRequest) {
        Team newTeam = teamService.addTeam(teamRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newTeam.getId())
            .toUri();
        return ResponseEntity.created(uri).body(newTeam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> editTeam(
        @PathVariable UUID id,
        @RequestBody TeamDTO teamRequest
    ) {
        return ResponseEntity.ok(teamService.editTeam(id, teamRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
