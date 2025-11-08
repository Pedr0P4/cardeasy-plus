import clsx from "clsx";
import { notFound } from "next/navigation";
import CloseButtonParallelRoute from "@/components/CloseButtonParallelRoute";
import CreateStageFormSection from "@/components/projects/forms/modals/CreateStageFormSection";
import { Api } from "@/services/api";

export default async function CreateStageModal({
  params,
}: Readonly<{
  params: Promise<{ project: string }>;
}>) {
  const { project: projectId } = await params;

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
          <CreateStageFormSection project={project} />
        </main>
      </div>
    </dialog>
  );
}
