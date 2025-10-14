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
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.services.ProjectService;
import ufrn.imd.cardeasy.services.TeamService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams/{teamId}")
public class ProjectController {

  private final TeamService teamService;
  private final ProjectService projectService;

  @GetMapping("/projects")
  public ResponseEntity<List<Project>> getProjects(@PathVariable UUID teamId) {
    List<Project> projects = teamService.getProjectsFromTeam(teamId);
    return ResponseEntity.ok(projects);
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<Project> getProject(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    return ResponseEntity.ok(project);
  }

  @PostMapping("/projects")
  public ResponseEntity<Project> addProject(
    @PathVariable UUID teamId,
    @RequestBody ProjectDTO projectRequest
  ) {
    Project newProject = teamService.addProjectOnTeam(teamId, projectRequest);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{projectId}")
      .buildAndExpand(newProject.getId())
      .toUri();
    return ResponseEntity.created(uri).body(newProject);
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<Project> editProject(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @RequestBody ProjectDTO projectRequest
  ) {
    Project editedProject = projectService.editTeamProject(
      teamId,
      projectId,
      projectRequest
    );
    return ResponseEntity.ok(editedProject);
  }

  @PostMapping("/projects/swap")
  public ResponseEntity<Void> swapProjectIndex(
    @PathVariable UUID teamId,
    @RequestBody Project project1,
    @RequestBody Project project2
  ) {
    projectService.swapProjects(teamId, project1, project2);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<Void> deleteProject(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId
  ) {
    teamService.deleteTeamProject(teamId, projectId);
    return ResponseEntity.ok().build();
  }
}
