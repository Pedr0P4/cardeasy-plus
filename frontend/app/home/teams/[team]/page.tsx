import { Api } from "@/services/api";
import type { UUID } from "crypto";

export default async function TeamPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
  const team = Api.server().participations().others(teamId);

  return <h1>{teamId}</h1>;
}
