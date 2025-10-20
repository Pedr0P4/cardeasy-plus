"use server";

import { cookies } from "next/headers";
import { api } from "./axios";

export type LoginData = {
  email: string;
  password: string;
};

export async function login(data: LoginData) {
  return api.post<string>("/accounts/auth", data).then(async (res) => {
    const _cookies = await cookies();
    _cookies.set("cardeasy@token", res.data);
  });
}
