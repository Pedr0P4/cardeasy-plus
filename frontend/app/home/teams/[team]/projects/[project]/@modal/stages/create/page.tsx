import type { UUID } from "crypto";
import Link from "next/link";
import { FaX } from "react-icons/fa6";
import CloseButtonParallelRoute from "@/components/CloseButtonParallelRoute";
import { Api } from "@/services/api";

export default async function CreateStageModal({
  params,
}: Readonly<{
  params: Promise<{ team: UUID; project: string }>;
}>) {
  const { project: projectId, team: teamId } = await params;

  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10));

  const participation = await Api.server().participations().get(teamId);

  return (
    <dialog open className="modal">
      <div className="modal-box flex flex-col gap-4">
        <CloseButtonParallelRoute />
      </div>
    </dialog>
  );
}
