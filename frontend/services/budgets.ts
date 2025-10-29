import { Service } from "./base/services";

export type Budget = {
  id: number;
  minValue: number;
  maxValue: number;
  currency: string;
  deadline?: number;
};

export type CreateBudgetData = {
  project: number;
  minValue: number;
  maxValue: number;
  currency: string;
  deadline?: number;
};

export type UpdateBudgetData = {
  minValue: number;
  maxValue: number;
  currency: string;
  deadline?: number;
};

export class BudgetsService extends Service {
  async create(data: CreateBudgetData) {
    return this.api.post<Budget>("/budgets", data).then((res) => res.data);
  }

  async update(id: number, data: UpdateBudgetData) {
    return this.api.put<Budget>(`/budgets/${id}`, data).then((res) => res.data);
  }

  async delete(id: number) {
    return this.api.delete(`/budgets/${id}`);
  }
}
