"use client";

import { useQuery } from "@tanstack/react-query";
import { format } from "date-fns";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";

interface Props {
  project: Project;
}

export default function ProjectHeader({ project }: Props) {
  const query = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const formatter = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: query.data?.budget?.currency ?? "BRL",
  });

  return (
    <div className="p-4 flex flex-col gap-0">
      <h1 className="font-bold text-2xl">{query.data.title}</h1>
      <p>{query.data.description}</p>
      {query.data.budget && (
        <>
          <hr className="w-1/6 my-1 border-base-300" />
          <p className="font-extralight italic text-sm mt-2">
            Verba de{" "}
            {query.data.budget.minValue === query.data.budget.maxValue ? (
              <span className="bg-base-100 p-1 rounded-md">
                {formatter.format(query.data.budget.minValue)}
              </span>
            ) : (
              <span className="bg-base-100 p-1 rounded-md">
                {formatter.format(query.data.budget.minValue)} ~{" "}
                {formatter.format(query.data.budget.maxValue)}
              </span>
            )}
            {query.data.budget.deadline && (
              <>
                {" até "}
                <span className="bg-base-100 p-1 rounded-md">
                  {format(new Date(query.data.budget.deadline), "dd/MM/uuuu")}
                </span>
              </>
            )}
          </p>
        </>
      )}
    </div>
  );
}
