import { Service } from "./base/services";

export type Stage = {
  id: number;
  name: string;
  current: boolean;
  description: string;
  expectedStartIn: number;
  expectedEndIn: number;
};

export type CreateStageDTO = {
  project: number;
  name: string;
  description: string;
  expectedStartIn?: number;
  expectedEndIn?: number;
};

export type UpdateStageDTO = {
  name: string;
  current: boolean;
  description: string;
  expectedStartIn?: number;
  expectedEndIn?: number;
};

export class StagesService extends Service {
  async get(id: number) {
    return this.api.get<Stage>(`/stages/${id}`).then((res) => res.data);
  }

  async all() {
    return this.api.get<Stage[]>("/stages").then((res) => res.data);
  }

  async create(data: CreateStageDTO) {
    return this.api.post<Stage>("/stages", data).then((res) => res.data);
  }

  async update(id: number, data: UpdateStageDTO) {
    return this.api.put<Stage>(`/stages/${id}`, data).then((res) => res.data);
  }

  async delete(id: number) {
    return this.api.delete(`/stages/${id}`);
  }
}
