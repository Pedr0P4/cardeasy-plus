import type { UUID } from "crypto";
import { Service } from "./base/services";
import { Account } from "./accounts";

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
  async participation(id: UUID) {
    return this.api.get<Participation>(`/teams/${id}`).then((res) => res.data);
  }

  async participations() {
    return this.api.get<Participation[]>(`/teams`).then((res) => res.data);
  }

  async create(data: CreateTeamData) {
    return this.api.post<Team>("/teams", data).then((res) => res.data);
  }

  async join(code: string) {
    return this.api.post<Team>(`/teams/join/${code}`).then((res) => res.data);
  }
}
