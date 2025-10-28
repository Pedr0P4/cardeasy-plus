import type { UUID } from "crypto";
import { Service } from "./base/services";

export type Project = {
  id: number;
  index: number;
  title: string;
  description: string;
  team: UUID;
};

export type CreateProjectData = {
  title: string;
  description: string;
  team: UUID;
};

export class ProjectsService extends Service {
  async get(id: number) {
    return this.api.get<Project>(`/projects/${id}`).then((res) => res.data);
  }

  async all() {
    return this.api.get<Project[]>("/projects").then((res) => res.data);
  }

  async create(data: CreateProjectData) {
    return this.api.post<Project>("/projects", data).then((res) => res.data);
  }

  async swap(first: number, second: number) {
    return this.api
      .post<Project>("/projects/swap", {
        first,
        second,
      })
      .then((res) => res.data);
  }
}
