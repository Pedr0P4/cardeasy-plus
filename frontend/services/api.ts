import type { AxiosInstance } from "axios";
import {
  type CookieValueTypes,
  getCookie as getClientCookie,
  setCookie as setClientCookie,
} from "cookies-next/client";
import {
  getCookie as getServerCookie,
  setCookie as setServerCookie,
} from "cookies-next/server";
import { AccountsService } from "./accounts";
import { Axios } from "./base/axios";
import { client } from "./base/client";
import { ImagesService } from "./image";
import { ProjectsService } from "./projects";
import { TeamsService } from "./teams";

export class Api {
  constructor(
    private getApi: (
      getCookie: (key: string) => Promise<CookieValueTypes>,
    ) => AxiosInstance,
    private getCookie: (key: string) => Promise<CookieValueTypes>,
    private setCookie: (key: string, value: string) => Promise<void>,
  ) {}

  public static client(): Api {
    return new Api(
      () => client,
      async (key) => getClientCookie(key),
      async (key, data) => setClientCookie(key, data),
    );
  }

  public static server(): Api {
    return new Api(
      (getCookie) => {
        const server = Axios.create();

        server.interceptors.request.use(async (config) => {
          const token = await getCookie("cardeasy@token");

          if (token) config.headers.Authorization = `Bearer ${token}`;

          return config;
        });

        return server;
      },
      async (key) => {
        const { cookies } = await import("next/headers");
        return await getServerCookie(key, { cookies });
      },
      async (key, data) => {
        const { cookies } = await import("next/headers");
        return await setServerCookie(key, data, { cookies });
      },
    );
  }

  public accounts() {
    return new AccountsService(
      this.getApi(this.getCookie),
      this.getCookie,
      this.setCookie,
    );
  }

  public teams() {
    return new TeamsService(
      this.getApi(this.getCookie),
      this.getCookie,
      this.setCookie,
    );
  }

  public projects() {
    return new ProjectsService(
      this.getApi(this.getCookie),
      this.getCookie,
      this.setCookie,
    );
  }

  public images() {
    return new ImagesService(
      this.getApi(this.getCookie),
      this.getCookie,
      this.setCookie,
    );
  }
}
