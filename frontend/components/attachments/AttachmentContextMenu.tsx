"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useState } from "react";
import { FaDownload, FaGear, FaPenRuler, FaTrash } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Attachment } from "@/services/attachments";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Toasts } from "@/services/toats";
import { saveAs } from "file-saver";

interface Props {
  attachment: Attachment;
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function AttachmentContextMenu({
  attachment,
  project,
  cardList,
  card,
}: Props) {
  const [isLoading, setIsLoading] = useState(false);

  const queryClient = useQueryClient();
  const deleteMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .attachments()
        .delete(attachment.id)
        .then(() => {
          Toasts.success("Anexo apagado com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
            ],
          });
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const downloadMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .attachments()
        .download(attachment.id)
        .then((file) => {
          Toasts.success("Anexo baixado com sucesso!");
          saveAs(file);
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const isPending =
    isLoading || downloadMutation.isPending || deleteMutation.isPending;

  return (
    <div className="absolute top-0 right-0 dropdown dropdown-end">
      <button tabIndex={0} type="button" className="btn btn-xs m-2">
        <FaGear />
      </button>
      <ul
        tabIndex={-1}
        className={clsx(
          "dropdown-content menu bg-base-100",
          "rounded-box z-1 w-64 p-2 shadow-sm",
          "border border-base-content",
        )}
      >
        <li>
          <Link
            onClick={(e) => e.currentTarget.blur()}
            href={`/home/teams/${project.team}/projects/${project.id}/card-lists/${cardList.id}/cards/${card.id}/attachments/${attachment.id}/edit`}
          >
            <FaPenRuler />
            Editar anexo
          </Link>
        </li>
        <li>
          <button
            type="button"
            disabled={isPending}
            onClick={(e) => {
              e.currentTarget.blur();
              setIsLoading(true);
              downloadMutation.mutate();
            }}
          >
            <FaDownload />
            Baixar anexo
          </button>
        </li>
        <li>
          <button
            type="button"
            disabled={isPending}
            onClick={(e) => {
              e.currentTarget.blur();
              setIsLoading(true);
              deleteMutation.mutate();
            }}
            className="text-primary"
          >
            <FaTrash />
            Remover cartão
          </button>
        </li>
      </ul>
    </div>
  );
}
