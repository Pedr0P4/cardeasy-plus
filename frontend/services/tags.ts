import { Service } from "./base/services";

export type Tag = {
  id: number;
  content: string;
};

export class TagsService extends Service {
  async search(
    project: number,
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 6,
  ) {
    return this.api
      .get<Page<Tag>>(
        `/tags/search?project=${project}&card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }

  async searchUsages(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 6,
  ) {
    return this.api
      .get<Page<Tag>>(
        `/tags/usages/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }
}
