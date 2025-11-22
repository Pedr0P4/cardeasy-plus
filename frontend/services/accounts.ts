import type { UUID } from "crypto";
import { Service } from "./base/services";
import type { ImageData } from "./image";

export type Account = {
  id: UUID;
  name: string;
  email: string;
  avatar?: ImageData;
};

export type LoginData = {
  email: string;
  password: string;
};

export type RegisterData = {
  avatar?: ImageData;
  name: string;
  email: string;
  password: string;
};

export type EditAccountData = {
  avatar?: ImageData;
  name: string;
  email: string;
  password: string;
  newPassword?: string;
};

export class AccountsService extends Service {
  async login(data: LoginData) {
    await this.logout();
    return this.api.post<string>("/accounts/auth", data).then(async (res) => {
      await this.setCookie("cardeasy@token", res.data);
    });
  }

  async logout() {
    await this.setCookie("cardeasy@token", "");
  }

  async register({ avatar, ...data }: RegisterData) {
    const form = new FormData();

    if (avatar) form.append("avatar", avatar.blob, avatar.filename);

    form.append(
      "account",
      new Blob([JSON.stringify(data)], {
        type: "application/json",
      }),
    );

    return this.api.post<string>("/accounts", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  async update({ avatar, ...data }: EditAccountData) {
    const form = new FormData();

    if (avatar) form.append("avatar", avatar.blob, avatar.filename);

    form.append(
      "body",
      new Blob([JSON.stringify(data)], {
        type: "application/json",
      }),
    );

    return this.api.put<string>("/accounts", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  async verify() {
    return this.api.get<Account>("/accounts/verify").then((res) => res.data);
  }
}
