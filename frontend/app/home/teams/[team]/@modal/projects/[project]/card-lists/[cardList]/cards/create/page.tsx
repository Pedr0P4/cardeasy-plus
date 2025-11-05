import clsx from "clsx";
import CloseButtonParallelRoute from "@/components/CloseButtonParallelRoute";
import CreateCardFormSection from "@/components/projects/forms/modals/CreateCardFormSection";
import { Api } from "@/services/api";

export default async function CreateCardModal({
  params,
}: Readonly<{
  params: Promise<{ project: string; cardList: string }>;
}>) {
  const { project: projectId, cardList: cardListId } = await params;

  const project = await Api.server()
    .projects()
    .get(Number.parseInt(projectId, 10));

  const cardList = await Api.server()
    .cardLists()
    .get(Number.parseInt(cardListId, 10));

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
          <CreateCardFormSection cardList={cardList} project={project} />
        </main>
      </div>
    </dialog>
  );
}
