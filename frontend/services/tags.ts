import { Service } from "./base/services";

export type Tag = {
  id: number;
  content: string;
  usages: number;
  used?: boolean;
};

export type CreateTagData = {
  content: string;
  project: number;
  card: number;
};

export type UpdateTagData = {
  content: string;
};

export class TagsService extends Service {
  async select(id: number, card: number) {
    return this.api.post(`/tags/${id}`, { card });
  }

  async deselect(id: number, card: number) {
    return this.api.delete(`/tags/${id}`, {
      data: {
        card,
      },
    });
  }

  async delete(id: number) {
    return this.api.delete(`/tags/${id}/all`);
  }

  async create(data: CreateTagData) {
    return this.api.post("/tags", data);
  }

  async update(id: number, data: UpdateTagData) {
    return this.api.put(`/tags/${id}`, data);
  }

  async searchCandidates(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 8,
  ) {
    return this.api
      .get<Page<Tag>>(
        `/tags/candidates/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }

  async search(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 8,
  ) {
    return this.api
      .get<Page<Tag>>(
        `/tags/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }
}
