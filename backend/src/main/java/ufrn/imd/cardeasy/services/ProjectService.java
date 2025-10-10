package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;
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

    public Project addProjectOnTeam(UUID id, ProjectDTO projectRequest) {
        Team team = teamService.findTeam(id);
        Project newProject = new Project(
            projectRequest.index(),
            projectRequest.title(),
            projectRequest.description(),
            team
        );
        projectsRepository.save(newProject);
        return newProject;
    }

    public void swapProjectsIndex(UUID id, ProjectDTO projectRequest1, ProjectDTO projectRequest2){
        Team team = teamService.findTeam(id);
        Project project1 = new Project(projectRequest1.index(), projectRequest1.title(), projectRequest1.description(),)
    }
}
