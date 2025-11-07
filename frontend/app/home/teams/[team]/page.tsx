import clsx from "clsx";
import type { UUID } from "crypto";
import {
  FaDiagramProject,
  FaGear,
  FaSquareCaretDown,
  FaSquareCaretUp,
  FaUserGroup,
} from "react-icons/fa6";
import Accodition from "@/components/accordition/Accodition";
import AccoditionButton from "@/components/accordition/AccoditionButton";
import AccordionContext from "@/components/accordition/context/accoditionContext";
import TabsContext from "@/components/tabs/context/tabsContext";
import Tab from "@/components/tabs/Tab";
import TabButton from "@/components/tabs/TabButton";
import TeamConfiguration from "@/components/teams/TeamConfiguration";
import TeamHeader from "@/components/teams/TeamHeader";
import TeamMembers from "@/components/teams/TeamMembers";
import TeamProjects from "@/components/teams/TeamProjects";
import { Api } from "@/services/api";
import { Role } from "@/services/participations";

export default async function TeamPage({
  params,
}: Readonly<{
  params: Promise<{ team: UUID }>;
}>) {
  const { team: teamId } = await params;
  const participation = await Api.server().participations().get(teamId);
  const participations = await Api.server().teams().participations(teamId);
  const isOwner = participation.role === Role.OWNER;

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
          <AccordionContext initial={true}>
            <Accodition>
              <TeamHeader participation={participation} />
            </Accodition>
            <div className="tabs tabs-lift">
              <AccoditionButton
                type="button"
                role="tab"
                className="tab"
                hiddenIcon={<FaSquareCaretUp className="size-4" />}
                showIcon={<FaSquareCaretDown className="size-4" />}
              />
              <TabButton name="projects">
                <FaDiagramProject className="size-4 me-2" /> Projetos
              </TabButton>
              <TabButton name="members">
                <FaUserGroup className="size-4 me-2" /> Membros
              </TabButton>
              {isOwner && (
                <TabButton name="config">
                  <FaGear className="size-4 me-2" /> Configurações
                </TabButton>
              )}
            </div>
          </AccordionContext>
        </section>
        <Tab name="projects">
          <section className="w-full flex flex-col gap-2 p-6">
            <TeamProjects participation={participation} />
          </section>
        </Tab>
        <Tab name="members">
          <section className="w-full flex flex-col gap-2 p-6">
            <TeamMembers
              participations={participations}
              participation={participation}
            />
          </section>
        </Tab>
        {isOwner && (
          <Tab name="config">
            <section className="w-full h-full flex flex-1 flex-col gap-2">
              <TeamConfiguration participation={participation} />
            </section>
          </Tab>
        )}
      </TabsContext>
    </main>
  );
}
