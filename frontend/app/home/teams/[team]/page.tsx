import type { UUID } from "crypto";

export default async function ProjectPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;

  return <h1>{teamId}</h1>;
}
