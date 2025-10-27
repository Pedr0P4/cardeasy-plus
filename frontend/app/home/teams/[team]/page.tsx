import clsx from "clsx";
import type { UUID } from "crypto";
import TeamProjects from "@/components/teams/TeamProjects";
import { Api } from "@/services/api";

export default async function TeamPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
  const participation = await Api.server().participations().get(teamId);
  const projects = await Api.server().teams().projects(teamId);

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-6 p-6",
      )}
    >
      <section className="w-full flex flex-row gap-2"></section>
      <TeamProjects
        projects={projects}
        role={participation.role}
        team={participation.team}
      />
    </main>
  );
}
