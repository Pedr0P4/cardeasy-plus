import clsx from "clsx";
import type { UUID } from "crypto";
import TeamProjects from "@/components/teams/TeamProjects";
import { Api } from "@/services/api";
import { FaDiagramProject, FaGear, FaUserGroup } from "react-icons/fa6";
import Tab from "@/components/tabs/Tab";
import TabsContext from "@/components/tabs/context/tabsContex";
import TabButton from "@/components/tabs/TabButton";
import { Role } from "@/services/teams";
import TeamMembers from "@/components/teams/TeamMembers";
import TeamConfiguration from "@/components/teams/TeamConfiguration";

export default async function TeamPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
  const participation = await Api.server().participations().get(teamId);
  const participations = await Api.server().teams().participations(teamId);
  const projects = await Api.server().teams().projects(teamId);
  const isRole = participation.role === Role.OWNER;

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-start flex-1",
      )}
    >
      <TabsContext initial="projects">
        <section
          className={clsx(
            "flex flex-col bg-base-200 border-base-300",
            "rounded-box w-full rounded-none border m-0 px-2",
          )}
        >
          <div className="p-4">
            <h1 className="font-bold text-2xl">{participation.team.title}</h1>
            <p>{participation.team.description}</p>
          </div>
          <div className="tabs tabs-lift">
            <TabButton name="projects">
              <FaDiagramProject className="size-4 me-2" /> Projetos
            </TabButton>
            <TabButton name="members">
              <FaUserGroup className="size-4 me-2" /> Membros
            </TabButton>
            {isRole && (
              <TabButton name="config">
                <FaGear className="size-4 me-2" /> Configurações
              </TabButton>
            )}
          </div>
        </section>
        <Tab name="projects">
          <section className="w-full flex flex-col gap-2 p-6">
            <TeamProjects
              projects={projects}
              role={participation.role}
              team={participation.team}
            />
          </section>
        </Tab>
        <Tab name="members">
          <section className="w-full flex flex-col gap-2 p-6">
            <TeamMembers
              participations={participations}
              role={participation.role}
              team={participation.team}
            />
          </section>
        </Tab>
        {isRole && (
          <Tab name="config">
            <section className="w-full h-full flex flex-1 flex-col gap-2">
              <TeamConfiguration
                role={participation.role}
                team={participation.team}
              />
            </section>
          </Tab>
        )}
      </TabsContext>
    </main>
  );
}
