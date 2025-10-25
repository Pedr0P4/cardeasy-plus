import Link from "next/link";
import { FaUserGroup, FaDiagramProject, FaHouse } from "react-icons/fa6";
import { Api } from "@/services/api";
import { UUID } from "crypto";

export default async function ProjectBreadcrumbs({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
  const team = await Api.server().teams().get(teamId);

  return (
    <>
      <li>
        <Link href="/home">
          <FaHouse />
          Início
        </Link>
      </li>
      <li>
        <span className="inline-flex items-center gap-2">
          <FaUserGroup />
          {team.title}
        </span>
      </li>
    </>
  );
}
