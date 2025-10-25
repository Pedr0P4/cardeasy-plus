import { Api } from "@/services/api";
import { UUID } from "crypto";
import Link from "next/link";
import {
  FaFileContract,
  FaHouse,
  FaPlus,
  FaUserGroup,
  FaUsers,
} from "react-icons/fa6";

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
