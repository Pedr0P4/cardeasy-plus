import clsx from "clsx";
import type { Project } from "@/services/projects";
import type { Role } from "@/services/teams";
import BudgetFormSection from "./forms/BudgetFormSection";
import EditProjectFormSection from "./forms/EditProjectFormSection";

interface Props {
  project: Project;
  role: Role;
}

export default function ProjectConfiguration({ project, role }: Props) {
  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-start justify-center",
      )}
    >
      <BudgetFormSection project={project} />
      <EditProjectFormSection project={project} role={role} />
    </main>
  );
}
