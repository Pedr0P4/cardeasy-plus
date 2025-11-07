import type { UUID } from "crypto";
import type { Account } from "./accounts";
import { Service } from "./base/services";
import type { Team } from "./teams";

export type Participation = {
  account: Account;
  team: Team;
  role: Role;
};

export type UpdateParticipationData = {
  account: UUID;
  team: UUID;
  role: Role;
};

export type DeleteParticipationData = {
  account: UUID;
  team: UUID;
};

export type ExitParticipationData = {
  team: UUID;
};

export enum Role {
  OWNER = "OWNER",
  ADMIN = "ADMIN",
  MEMBER = "MEMBER",
}

export class ParticipationsService extends Service {
  async get(id: UUID) {
    return this.api
      .get<Participation>(`/participations/${id}`)
      .then((res) => res.data);
  }

  async search(page: number = 0, query: string = "") {
    return this.api
      .get<Page<Participation>>(
        `/participations/search?page=${page}&query=${query}`,
      )
      .then((res) => res.data);
  }

  async update(data: UpdateParticipationData) {
    return this.api
      .put<Participation>("/participations", data)
      .then((res) => res.data);
  }

  async delete(data: DeleteParticipationData) {
    return this.api.delete("/participations", { data });
  }

  async exit(data: ExitParticipationData) {
    return this.api.delete("/participations/exit", { data });
  }
}
