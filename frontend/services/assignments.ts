import { Service } from "./base/services";
import type { Participation } from "./participations";

export class AssignmentsService extends Service {
  async search(
    card: number,
    page: number = 0,
    query: string = "",
    itemsPerPage: number = 6,
  ) {
    return this.api
      .get<Page<Participation>>(
        `/assignments/search?card=${card}&page=${page}&query=${query}&itemsPerPage=${itemsPerPage}`,
      )
      .then((res) => res.data);
  }
}
