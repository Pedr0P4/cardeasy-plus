export default async function ProjectPage({
  params,
}: {
  params: Promise<{ team: string; project: string }>;
}) {
  const { team: teamId, project: projectId } = await params;

  return (
    <h1>
      {teamId}/{projectId}
    </h1>
  );
}
