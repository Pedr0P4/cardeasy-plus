import type { UUID } from "crypto";
import Link from "next/link";
import { FaHouse, FaUsers } from "react-icons/fa6";
import { Api } from "@/services/api";

export default async function ProjectBreadcrumbs({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
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
        <span className="inline-flex items-center gap-2">
          <FaUsers />
          {participation.team.title}
        </span>
      </li>
    </>
  );
}
