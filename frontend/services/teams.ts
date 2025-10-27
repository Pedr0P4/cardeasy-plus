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

export class TeamsService extends Service {
  async participations(id: UUID) {
    return this.api
      .get<Participation[]>(`/teams/${id}/participations`)
      .then((res) => res.data);
  }

  async projects(id: UUID) {
    return this.api.get<Project[]>(`/teams/${id}/projects`).then((res) => res.data);
  }

  async create(data: CreateTeamData) {
    return this.api.post<Team>("/teams", data).then((res) => res.data);
  }

  async join(code: string) {
    return this.api.post<Team>(`/teams/join/${code}`).then((res) => res.data);
  }
}
