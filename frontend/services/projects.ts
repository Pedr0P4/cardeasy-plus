"use server";

import { cookies } from "next/headers";
import { api } from "./axios";
import type { Team } from "./teams";

export type Project = {
  id: number;
  title: string;
  description: string;
  team: Team;
};

export async function getProject(id: number) {
  const _cookies = await cookies();

  return api
    .get<Project>(`/projects/${id}`, {
      headers: {
        Authorization: `Bearer ${_cookies.get("cardeasy@token")?.value}`,
      },
    })
    .then((res) => res.data);
}
