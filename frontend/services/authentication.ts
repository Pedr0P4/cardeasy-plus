import { useAuth } from "@/stores/auth";
import { api } from "./axios";

export type LoginData = {
  email: string;
  password: string;
};

export async function login(data: LoginData) {
  return api.post<string>("/accounts/auth", data).then((res) => {
    useAuth.getState().setToken(res.data);
  });
}
