import clsx from "clsx";
import type { Role, Team } from "@/services/teams";
import EditTeamFormSection from "./EditTeamFormSection";
import InviteCodeTeamFormSection from "./InviteCodeTeamFormSection";

interface Props {
  team: Team;
  role: Role;
}

export default function TeamConfiguration({ team, role }: Props) {
  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-start justify-center",
      )}
    >
      <InviteCodeTeamFormSection team={team} />
      <EditTeamFormSection team={team} role={role} />
    </main>
  );
}
