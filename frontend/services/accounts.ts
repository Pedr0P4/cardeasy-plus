import type { UUID } from "crypto";
import { api } from "./axios";
import type { ImageData } from "./image";

export type RegisterData = {
  avatar?: ImageData;
  name: string;
  email: string;
  password: string;
};

export async function register({ avatar, ...data }: RegisterData) {
  const form = new FormData();

  if (avatar) form.append("avatar", avatar.blob, avatar.filename);

  form.append(
    "account",
    new Blob([JSON.stringify(data)], {
      type: "application/json",
    }),
  );

  return api.post<string>("/accounts", form, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export type Account = {
  id: UUID;
  name: string;
  email: string;
  avatar?: ImageData;
};

export async function verify(token: string) {
  return api.get<Account>("/accounts/verify", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}
