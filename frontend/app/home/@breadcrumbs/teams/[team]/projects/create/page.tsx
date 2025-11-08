import type { UUID } from "crypto";
import Link from "next/link";
import { notFound } from "next/navigation";
import { FaHouse, FaPlus, FaUsers } from "react-icons/fa6";
import { Api } from "@/services/api";

export default async function ProjectBreadcrumbs({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;

  const participation = await Api.server()
    .participations()
    .get(teamId)
    .catch(() => notFound());

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
          <FaPlus />
          Criar novo projeto
        </span>
      </li>
    </>
  );
}
