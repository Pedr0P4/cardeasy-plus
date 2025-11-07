"use client";

import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useState } from "react";
import { FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import Input from "../Input";
import ProjectStageItem from "./ProjectStageItem";

interface Props {
  project: Project;
  role: Role;
}

export default function ProjectStages({ project, role }: Props) {
  const [searchQuery, setSearchQuery] = useState("");
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const projectQuery = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const query = useInfiniteQuery({
    queryKey: ["projects", project.id, "stages", `query-${searchQuery}`],
    queryFn: ({ pageParam }) =>
      Api.client()
        .stages()
        .search(projectQuery.data.id, pageParam, searchQuery),
    getNextPageParam: (lastPageData) => {
      if (lastPageData.page < lastPageData.lastPage) {
        return lastPageData.page + 1;
      }
      return undefined;
    },
    select: (data) => {
      return data.pages.flatMap((page) => page.items);
    },
    initialPageParam: 0,
    initialData: {
      pages: [],
      pageParams: [],
    },
  });

  return (
    <>
      <Input
        name="title"
        type="text"
        placeholder="Pesquisar por título ou descrição"
        label="Pesquisar por título ou descrição"
        className="mb-4"
        icon={FaMagnifyingGlass}
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data.map((stage) => {
          return (
            <ProjectStageItem
              key={`stage-${stage.id}`}
              project={projectQuery.data}
              stage={stage}
              role={role}
            />
          );
        })}
        {isAdmin && (
          <li className="w-full">
            <Link
              href={`/home/teams/${projectQuery.data.team}/projects/${projectQuery.data.id}/stages/create`}
              className={clsx(
                "btn btn-soft btn-neutral min-h-22 flex h-full flex-row",
                "items-center justify-center rounded-md px-6 py-4",
                "font-bold text-lg",
              )}
            >
              <FaPlus />
              Criar nova etapa
            </Link>
          </li>
        )}
      </ul>
    </>
  );
}
