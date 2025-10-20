import { api } from "./axios";
import { ImageData } from "./image";

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

  return api
    .post<string>("/accounts", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {});
}
