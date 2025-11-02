import type { UUID } from "crypto";
import { Service } from "./base/services";
import type { Stage } from "./stages";

export type Budget = {
  id: number;
  minValue: number;
  maxValue: number;
  currency: string;
  deadline?: number;
};

export type Project = {
  id: number;
  index: number;
  title: string;
  description: string;
  team: UUID;
  budget?: Budget;
};

export type CreateProjectData = {
  title: string;
  description: string;
  team: UUID;
};

export type UpdateProjectData = {
  title: string;
  description: string;
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

  async update(id: number, data: UpdateProjectData) {
    return this.api
      .put<Project>(`/projects/${id}`, data)
      .then((res) => res.data);
  }

  async delete(id: number) {
    return this.api.delete(`/projects/${id}`);
  }

  async swap(first: number, second: number) {
    return this.api
      .post<Project>("/projects/swap", {
        first,
        second,
      })
      .then((res) => res.data);
  }

  async stages(id: number) {
    return this.api
      .get<Stage[]>(`/stages/project/${id}`)
      .then((res) => res.data);
  }
}
