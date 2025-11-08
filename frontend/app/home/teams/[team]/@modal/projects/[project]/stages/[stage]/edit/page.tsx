import clsx from "clsx";
import { notFound } from "next/navigation";
import CloseButtonParallelRoute from "@/components/CloseButtonParallelRoute";
import EditStageFormSection from "@/components/projects/forms/modals/EditStageFormSection";
import { Api } from "@/services/api";

export default async function EditStageModal({
  params,
}: Readonly<{
  params: Promise<{ stage: string; project: string }>;
}>) {
  const { project: projectId, stage: stageId } = await params;

  const stage = await Api.server()
    .stages()
    .get(Number.parseInt(stageId, 10))
    .catch(() => notFound());

  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10))
    .catch(() => notFound());

  return (
    <dialog open className="modal">
      <div
        className={clsx(
          "modal-box flex flex-col gap-4 !px-0 !pb-0 !pt-2",
          "scrollbar scrollbar-thin scrollbar-thumb-base-content",
          "scrollbar-track-base-200 relative",
        )}
      >
        <CloseButtonParallelRoute />
        <main className="relative w-full h-full flex flex-1 flex-col">
          <EditStageFormSection stage={stage} project={project} />
        </main>
      </div>
    </dialog>
  );
}
