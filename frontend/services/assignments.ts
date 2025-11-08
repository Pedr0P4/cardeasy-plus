import type { UUID } from "crypto";
import { Service } from "./base/services";
import type { Role } from "./participations";

export type Assignment = {
  team: UUID;
  account: UUID;
  name: string;
  email: string;
  role: Role;
};

export type AssignmentCandidate = {
  team: UUID;
  account: UUID;
  name: string;
  email: string;
  role: Role;
  assigned: boolean;
};

export type CreateAssignmentData = {
  card: number;
  account: UUID;
};

export type DeleteAssignmentData = {
  card: number;
  account: UUID;
};

export class AssignmentsService extends Service {
  async delete(data: DeleteAssignmentData) {
    return this.api.delete("/assignments", {
      data,
    });
  }

  async create(data: CreateAssignmentData) {
    return this.api.post("/assignments", data);
  }

  async search(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 6,
  ) {
    return this.api
      .get<Page<Assignment>>(
        `/assignments/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }

  async searchCandidates(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 6,
  ) {
    return this.api
      .get<Page<AssignmentCandidate>>(
        `/assignments/candidates/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }
}
