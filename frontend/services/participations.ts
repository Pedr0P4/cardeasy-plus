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
  accountId: UUID;
  teamId: UUID;
  role: Role;
};

export type DeleteParticipationData = {
  accountId: UUID;
  teamId: UUID;
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

  async all() {
    return this.api
      .get<Participation[]>(`/participations`)
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
}
