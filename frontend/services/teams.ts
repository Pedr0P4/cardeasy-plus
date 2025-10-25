import type { UUID } from "crypto";
import { Service } from "./base/services";

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
  async get(id: UUID) {
    return this.api.get<Team>(`/teams/${id}`).then((res) => res.data);
  }

  async all() {
    return this.api.get<Team[]>(`/teams`).then((res) => res.data);
  }

  async create(data: CreateTeamData) {
    return this.api.post<Team>("/teams", data).then((res) => res.data);
  }
}
