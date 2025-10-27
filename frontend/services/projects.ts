import { Service } from "./base/services";
import type { Team } from "./teams";

export type Project = {
  id: number;
  title: string;
  description: string;
  team: Team;
};

export class ProjectsService extends Service {
  async get(id: number) {
    return this.api.get<Project>(`/projects/${id}`).then((res) => res.data);
  }

  async all() {
    return this.api.get<Project[]>("/projects").then((res) => res.data);
  }
}
