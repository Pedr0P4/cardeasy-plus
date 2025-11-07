import type { UUID } from "crypto";
import { Service } from "./base/services";
import type { Participation } from "./participations";

export type Team = {
  id: UUID;
  title: string;
  description: string;
  participations: number;
  code?: string;
};

export type CreateTeamData = {
  title: string;
  description: string;
};

export type UpdateTeamData = {
  title: string;
  description: string;
};

export type GeneratedCode = {
  code: string;
};

export class TeamsService extends Service {
  async participations(id: UUID, page: number = 0, query: string = "") {
    return this.api
      .get<Page<Participation>>(
        `/teams/${id}/participations/search?page=${page}&query=${query}`,
      )
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

  async deleteCode(id: UUID) {
    return this.api.delete(`/teams/${id}/code`);
  }

  async generateCode(id: UUID) {
    return this.api
      .post<GeneratedCode>(`/teams/${id}/code/generate`)
      .then((res) => res.data.code);
  }

  async join(code: string) {
    return this.api.post<Team>(`/teams/join/${code}`).then((res) => res.data);
  }

  async transfer(id: UUID, to: UUID) {
    return this.api.post<Team>(`/teams/${id}/transfer`, {
      account: to,
    });
  }
}
