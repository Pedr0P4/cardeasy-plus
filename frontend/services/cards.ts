import { Service } from "./base/services";

export type Card = {
  id: number;
  index: number;
  title: string;
  description: string;
  cardList: number;
};

export type CreateCardData = {
  cardList: number;
  title: string;
  description: string;
};

export type UpdateCardData = {
  title: string;
  description: string;
};

export class CardsService extends Service {
  async get(id: number) {
    return this.api.get<Card>(`/cards/${id}`).then((res) => res.data);
  }

  async create(data: CreateCardData) {
    return this.api.post<Card>("/cards", data).then((res) => res.data);
  }

  async update(id: number, data: UpdateCardData) {
    return this.api.put<Card>(`/cards/${id}`, data).then((res) => res.data);
  }

  async delete(id: number) {
    return this.api.delete(`/cards/${id}`);
  }

  async move(cardList: number, card: number, index: number) {
    return this.api.post(`/card-lists/${cardList}/cards/move`, {
      card,
      index,
    });
  }

  async search(cardList: number, page: number = 0, query: string = "") {
    return this.api
      .get<Page<Card>>(
        `/cards/search?list=${cardList}&page=${page}&query=${query}`,
      )
      .then((res) => res.data);
  }
}
