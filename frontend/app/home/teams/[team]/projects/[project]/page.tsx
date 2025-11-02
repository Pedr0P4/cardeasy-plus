import clsx from "clsx";
import type { UUID } from "crypto";
import { format } from "date-fns";
import {
  FaCalendarDays,
  FaGear,
  FaSquareCaretDown,
  FaSquareCaretUp,
  FaTrello,
} from "react-icons/fa6";
import Accodition from "@/components/accordition/Accodition";
import AccoditionButton from "@/components/accordition/AccoditionButton";
import AccordionContext from "@/components/accordition/context/accoditionContext";
import ProjectCardLists from "@/components/projects/ProjectCardLists";
import ProjectConfiguration from "@/components/projects/ProjectConfiguration";
import ProjectStages from "@/components/projects/ProjectStages";
import TabsContext from "@/components/tabs/context/tabsContext";
import Tab from "@/components/tabs/Tab";
import TabButton from "@/components/tabs/TabButton";
import { Api } from "@/services/api";
import { Role } from "@/services/teams";

export default async function ProjectPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID; project: string }>;
}>) {
  const { team: teamId, project: projectId } = await params;

  const participation = await Api.server().participations().get(teamId);
  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10));

  const stages = await Api.server()
    .projects()
    .stages(Number.parseInt(projectId, 10));

  const cardLists = await Api.server()
    .projects()
    .cardList(Number.parseInt(projectId, 10));

  const isAdmin = [Role.OWNER, Role.ADMIN].includes(participation.role);

  const formatter = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: project?.budget?.currency ?? "BRL",
  });

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-start flex-1",
      )}
    >
      <TabsContext initial="cards">
        <section
          className={clsx(
            "flex flex-col bg-base-200 border-base-300",
            "rounded-box w-full rounded-none border m-0 px-2",
          )}
        >
          <AccordionContext initial={true}>
            <Accodition>
              <div className="p-4 flex flex-col gap-0">
                <h1 className="font-bold text-2xl">{project.title}</h1>

                <p>{project.description}</p>
                {project.budget && (
                  <>
                    <hr className="w-1/6 my-1 border-base-300" />
                    <p className="font-extralight italic text-sm mt-2">
                      Verba de{" "}
                      <span className="bg-base-100 p-1 rounded-md">
                        {formatter.format(project.budget.minValue)} ~{" "}
                        {formatter.format(project.budget.maxValue)}
                      </span>
                      {project.budget.deadline && (
                        <>
                          {" até "}
                          <span className="bg-base-100 p-1 rounded-md">
                            {format(
                              new Date(project.budget.deadline),
                              "dd/MM/uuuu",
                            )}
                          </span>
                        </>
                      )}
                    </p>
                  </>
                )}
              </div>
            </Accodition>
            <div className="tabs tabs-lift">
              <AccoditionButton
                type="button"
                role="tab"
                className="tab"
                hiddenIcon={<FaSquareCaretUp className="size-4" />}
                showIcon={<FaSquareCaretDown className="size-4" />}
              />
              <TabButton name="cards">
                <FaTrello className="size-4 me-2" /> Cartões
              </TabButton>
              <TabButton name="stages">
                <FaCalendarDays className="size-4 me-2" /> Etapas
              </TabButton>
              {isAdmin && (
                <TabButton name="config">
                  <FaGear className="size-4 me-2" /> Configurações
                </TabButton>
              )}
            </div>
          </AccordionContext>
        </section>
        <Tab name="cards">
          <section
            className={clsx(
              "w-full flex flex-1 flex-col gap-2 p-6 overflow-x-auto",
              "scrollbar scrollbar-thin scrollbar-thumb-base-content",
              "scrollbar-track-base-200",
            )}
          >
            <ProjectCardLists
              project={project}
              role={participation.role}
              cardLists={cardLists}
            />
          </section>
        </Tab>
        <Tab name="stages">
          <section className="w-full flex flex-col gap-2 p-6">
            <ProjectStages
              project={project}
              role={participation.role}
              stages={stages}
            />
          </section>
        </Tab>
        {isAdmin && (
          <Tab name="config">
            <section className="w-full h-full flex flex-1 flex-col gap-2">
              <ProjectConfiguration
                project={project}
                role={participation.role}
              />
            </section>
          </Tab>
        )}
      </TabsContext>
    </main>
  );
}
