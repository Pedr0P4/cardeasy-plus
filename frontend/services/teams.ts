import type { UUID } from "crypto";
import type { Account } from "./accounts";
import { Service } from "./base/services";
import type { Project } from "./projects";

export type Participation = {
  account: Account;
  team: Team;
  role: Role;
};

export enum Role {
  OWNER = "OWNER",
  ADMIN = "ADMIN",
  MEMBER = "MEMBER",
}

export type Team = {
  id: UUID;
  title: string;
  description: string;
  participations: number;
};

export type CreateTeamData = {
  title: string;
  description: string;
};

export type UpdateTeamData = {
  title: string;
  description: string;
};

export class TeamsService extends Service {
  async participations(id: UUID) {
    return this.api
      .get<Participation[]>(`/teams/${id}/participations`)
      .then((res) => res.data);
  }

  async projects(id: UUID) {
    return this.api
      .get<Project[]>(`/teams/${id}/projects`)
      .then((res) => res.data);
  }

  async create(data: CreateTeamData) {
    return this.api.post<Team>("/teams", data).then((res) => res.data);
  }

  async update(id: UUID, data: UpdateTeamData) {
    return this.api.put<Team>(`/teams/${id}`, data).then((res) => res.data);
  }

  async delete(id: UUID) {
    return this.api.delete(`/teams/${id}`);
  }

  async join(code: string) {
    return this.api.post<Team>(`/teams/join/${code}`).then((res) => res.data);
  }
}
