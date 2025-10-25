import type { AxiosInstance } from "axios";
import type { CookieValueTypes } from "cookies-next/client";

export abstract class Service {
  constructor(
    protected api: AxiosInstance,
    protected getCookie: (key: string) => Promise<CookieValueTypes>,
    protected setCookie: (key: string, value: string) => Promise<void>,
  ) {}
}
