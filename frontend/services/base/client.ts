import { getCookie as getClientCookie } from "cookies-next/client";
import { Axios } from "./axios";

const client = Axios.create();

client.interceptors.request.use(async (config) => {
  const token = await getClientCookie("cardeasy@token");

  if (token) config.headers.Authorization = `Bearer ${token}`;

  return config;
});

export { client };
