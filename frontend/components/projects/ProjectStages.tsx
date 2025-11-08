"use client";

import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useState } from "react";
import { FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import Input from "../Input";
import Pagination from "../Pagination";
import ProjectStageItem from "./ProjectStageItem";

interface Props {
  project: Project;
  role: Role;
}

export default function ProjectStages({ project, role }: Props) {
  const [searchQuery, setSearchQuery] = useState("");
  const [page, setPage] = useState(0);
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const projectQuery = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const query = useQuery({
    queryKey: [
      "projects",
      project.id,
      "stages",
      `page-${page}`,
      `query-${searchQuery}`,
    ],
    queryFn: () =>
      Api.client().stages().search(projectQuery.data.id, page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
      total: 0,
    },
  });

  return (
    <>
      <div className="flex flex-col md:flex-row gap-4 mb-4 md:items-end">
        {isAdmin && (
          <Link
            href={`/home/teams/${projectQuery.data.team}/projects/${projectQuery.data.id}/stages/create`}
            className="btn btn-neutral"
          >
            <FaPlus />
            Criar nova etapa
          </Link>
        )}
        <Input
          name="search"
          type="text"
          placeholder="Pesquisar por título ou descrição"
          icon={FaMagnifyingGlass}
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data.items.map((stage) => {
          return (
            <ProjectStageItem
              key={`stage-${stage.id}`}
              project={projectQuery.data}
              stage={stage}
              role={role}
            />
          );
        })}
      </ul>
      <Pagination
        className="outline outline-2 rounded-lg outline-base-100"
        current={page}
        last={query.data.lastPage}
        onChange={setPage}
      />
    </>
  );
}
