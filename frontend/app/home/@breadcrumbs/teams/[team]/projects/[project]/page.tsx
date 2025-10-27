import Link from "next/link";
import {
  FaDiagramProject,
  FaHouse,
  FaUserGroup,
  FaUsers,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import { UUID } from "crypto";

export default async function ProjectBreadcrumbs({
  params,
}: Readonly<{
  params: Promise<{ team: UUID, project: string }>;
}>) {
  const { project: projectId, team: teamId } = await params;
  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10));

  const participation = await Api.server().participations().get(teamId);

  return (
    <>
      <li>
        <Link href="/home">
          <FaHouse />
          Início
        </Link>
      </li>
      <li>
        <Link href={`/home/teams/${participation.team.id}`}>
          <FaUsers />
          {participation.team.title}
        </Link>
      </li>
      <li>
        <span className="inline-flex items-center gap-2">
          <FaDiagramProject />
          {project.title}
        </span>
      </li>
    </>
  );
}
