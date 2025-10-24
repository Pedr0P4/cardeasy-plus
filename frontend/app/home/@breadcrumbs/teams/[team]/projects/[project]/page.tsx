import Link from "next/link";
import { FaHome, FaProjectDiagram } from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { Api } from "@/services/api";

export default async function ProjectBreadcrumbs({
  params,
}: Readonly<{
  params: Promise<{ project: string }>;
}>) {
  const { project: projectId } = await params;
  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10));

  return (
    <>
      <li>
        <Link href="/home">
          <FaHome />
          Início
        </Link>
      </li>
      <li>
        <Link href={`/home/teams/${project.team.id}`}>
          <FaUserGroup />
          {project.team.title}
        </Link>
      </li>
      <li>
        <span className="inline-flex items-center gap-2">
          <FaProjectDiagram />
          {project.title}
        </span>
      </li>
    </>
  );
}
