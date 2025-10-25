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

export class ParticipationsService extends Service {
  async get(id: UUID) {
    return this.api
      .get<Participation>(`participations/me/${id}`)
      .then((res) => res.data);
  }

  async others(id: UUID) {
    return this.api
      .get<Participation>(`participations/${id}`)
      .then((res) => res.data);
  }

  async all() {
    return this.api
      .get<Participation[]>(`/participations/me`)
      .then((res) => res.data);
  }
}
