import type { UUID } from "crypto";
import Link from "next/link";
import {
  FaDiagramProject,
  FaHouse,
  FaUserGroup,
  FaUsers,
} from "react-icons/fa6";
import { Api } from "@/services/api";

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
          <FaUsers />
          {team.title}
        </span>
      </li>
    </>
  );
}
