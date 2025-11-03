"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { Api } from "@/services/api";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import BudgetFormSection from "./forms/BudgetFormSection";
import EditProjectFormSection from "./forms/EditProjectFormSection";

interface Props {
  project: Project;
  role: Role;
}

export default function ProjectConfiguration({ project, role }: Props) {
  const query = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-start justify-center",
      )}
    >
      <BudgetFormSection project={query.data} />
      <EditProjectFormSection project={query.data} role={role} />
    </main>
  );
}
