"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useState } from "react";
import { FaGear, FaPenRuler, FaTrash } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function ProjectCardContextMenu({
  project,
  cardList,
  card,
}: Props) {
  const [isLoading, setIsLoading] = useState(false);
  const queryClient = useQueryClient();
  const deleteMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .cards()
        .delete(card.id)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "card-lists",
              cardList.id,
              "cards",
            ],
          });

          queryClient.removeQueries({
            queryKey: [
              "projects",
              project.id,
              "card-lists",
              cardList.id,
              "cards",
              card.id,
            ],
          });
        })
        .finally(() => setIsLoading(false));
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const isPending = isLoading || deleteMutation.isPending;

  return (
    <div className="absolute top-0 right-0 dropdown dropdown-end">
      <button tabIndex={0} type="button" className="btn btn-xs my-1.5 mr-4">
        <FaGear />
      </button>
      <ul
        tabIndex={-1}
        className={clsx(
          "dropdown-content menu bg-base-100",
          "rounded-box z-40 w-64 p-2 shadow-sm",
          "border border-base-content",
        )}
      >
        <li>
          <Link
            onClick={(e) => e.currentTarget.blur()}
            href={`/home/teams/${project.team}/projects/${project.id}/card-lists/${cardList.id}/cards/${card.id}/edit`}
          >
            <FaPenRuler />
            Editar cartão
          </Link>
        </li>
        <li>
          <button
            disabled={isPending}
            onClick={(e) => {
              e.currentTarget.blur();
              setIsLoading(true);
              deleteMutation.mutate();
            }}
            type="button"
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
