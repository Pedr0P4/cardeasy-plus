import { Project } from "@/services/projects";
import { Role } from "@/services/teams";
import clsx from "clsx";
import EditProjectFormSection from "./EditProjectFormSection";
import BudgetFormSection from "./BudgetFormSection";

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
