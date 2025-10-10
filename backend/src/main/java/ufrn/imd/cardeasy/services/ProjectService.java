package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@RequiredArgsConstructor
public class ProjectService {

    private final TeamService teamService;
    private final ProjectsRepository projectsRepository;

    public List<Project> getProjectsFromTeam(UUID id) {
        Team team = teamService.findTeam(id);
        return team.getProjects();
    }

    public Project getProjectFromTeam(UUID teamId, int projectId){
      Team team = teamService.findTeam(teamId);
      Project project = projectsRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found"));
      if(team.getProjects().contains(project)){
        return project;
      } else {
        throw new EntityNotFoundException("Project not found on team" + teamId);
      }
    }

    public Project addProjectOnTeam(UUID id, ProjectDTO projectRequest) {
        Team team = teamService.findTeam(id);
        Project newProject = new Project(team.getProjects().size()+1, projectRequest.title(), projectRequest.description(), team);
        projectsRepository.save(newProject);
        return newProject;
    }

    @Transactional
    public void swapProjectsIndex(UUID id, Project projectRequest1, Project projectRequest2){
        Team team = teamService.findTeam(id);
        List<Project> projects = team.getProjects();
        if(projects.contains(projectRequest1) && projects.contains(projectRequest2)){
          int tmp = projectRequest1.getIndex();
          projectRequest1.setIndex(0);
          projectsRepository.save(projectRequest1);
          projectRequest1.setIndex(projectRequest2.getIndex());
          projectRequest2.setIndex(tmp);
          projectsRepository.save(projectRequest1);
          projectsRepository.save(projectRequest2);
        }
    }
}
