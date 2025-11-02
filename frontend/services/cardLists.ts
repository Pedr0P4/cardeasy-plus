import { Service } from "./base/services";

export type CardList = {
  id: number;
  index: number;
  title: string;
  project: number;
};

export type CreateCardListData = {
  title: string;
  project: number;
};

export type UpdateProjectData = {
  title: string;
};

export class CardListsService extends Service {
  async get(id: number) {
    return this.api.get<CardList>(`/card-lists/${id}`).then((res) => res.data);
  }

  async create(data: CreateCardListData) {
    return this.api.post<CardList>("/card-lists", data).then((res) => res.data);
  }

  async update(id: number, data: UpdateProjectData) {
    return this.api
      .put<CardList>(`/card-lists/${id}`, data)
      .then((res) => res.data);
  }

  async delete(id: number) {
    return this.api.delete(`/card-lists/${id}`);
  }

  async swap(first: number, second: number) {
    return this.api
      .post<CardList>("/card-lists/swap", {
        first,
        second,
      })
      .then((res) => res.data);
  }
}
