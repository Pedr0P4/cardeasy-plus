import { UUID } from "crypto";

export default async function ProjectPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID; project: string }>;
}>) {
  const { team: teamId, project: projectId } = await params;

  return (
    <h1>
      {teamId}/{projectId}
    </h1>
  );
}
