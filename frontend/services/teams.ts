"use server";

import type { UUID } from "crypto";
import { cookies } from "next/headers";
import { api } from "./axios";

export type Team = {
  id: UUID;
  title: string;
  description: string;
};

export async function getTeam(id: UUID) {
  const _cookies = await cookies();

  return api
    .get<Team>(`/teams/${id}`, {
      headers: {
        Authorization: `Bearer ${_cookies.get("cardeasy@token")?.value}`,
      },
    })
    .then((res) => res.data);
}
