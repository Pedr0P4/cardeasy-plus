"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useState } from "react";
import { FaBookmark, FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import Input from "@/components/Input";
import Pagination from "@/components/Pagination";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Toasts } from "@/services/toats";
import TagCandidateItem from "./TagCandidateItem";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function TagsSection({ project, cardList, card }: Props) {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const queryCandidates = useQuery({
    queryKey: [
      "projects",
      project.id,
      "cards-lists",
      cardList.id,
      "cards",
      card.id,
      "tags",
      "candidates",
      `page-${page}`,
      `query-${searchQuery}`,
    ],
    queryFn: () =>
      Api.client().tags().searchCandidates(card.id, page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
      total: 0,
    },
  });

  const queryClient = useQueryClient();

  const updateMutation = useMutation({
    mutationFn: async ({ id, content }: { id: number; content: string }) => {
      return await Api.client()
        .tags()
        .update(id, { content })
        .then(() => {
          Toasts.success("Etiqueta atualizada com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards"
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

  const createMutation = useMutation({
    mutationFn: async (content: string) => {
      return await Api.client()
        .tags()
        .create({
          card: card.id,
          content,
          project: project.id,
        })
        .then(() => {
          Toasts.success("Etiqueta criada com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
              card.id,
              "tags",
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

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      return await Api.client()
        .tags()
        .delete(id)
        .then(() => {
          Toasts.success("Etiqueta apagada com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards"
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

  const selectMutation = useMutation({
    mutationFn: async (id: number) => {
      return await Api.client()
        .tags()
        .select(id, card.id)
        .then(() => {
          Toasts.success("Etiqueta marcada com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
              card.id,
              "tags",
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

  const deselectMutation = useMutation({
    mutationFn: async (id: number) => {
      return await Api.client()
        .tags()
        .deselect(id, card.id)
        .then(() => {
          Toasts.success("Etiqueta desmarcada com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
              card.id,
              "tags",
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

  const isPending =
    isLoading ||
    selectMutation.isPending ||
    deselectMutation.isPending ||
    createMutation.isPending ||
    deleteMutation.isPending ||
    updateMutation.isPending;

  return (
    <>
      <h1
        className={clsx(
          "text-2xl font-semibold",
          "text-xl self-start",
          "py-4 px-4 sm:px-6  bg-base-100 w-full",
          "flex flex-row items-center gap-2",
        )}
      >
        <FaBookmark className="size-6" />
        Etiquetas
      </h1>
      <div
        className={clsx(
          "flex flex-col gap-2 bg-base-200 border-base-300",
          "rounded-box w-full rounded-none border",
          "py-4 px-4 sm:px-6 m-0 pb-6",
        )}
      >
        <div className="flex flex-col md:flex-row gap-4 mb-2 md:items-end">
          <Input
            disabled={isPending}
            name="search"
            type="text"
            placeholder="Pesquisar por etiqueta"
            icon={FaMagnifyingGlass}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button
            disabled={!searchQuery || searchQuery.length < 3}
            onClick={() => {
              setIsLoading(true);
              createMutation.mutate(searchQuery);
            }}
            type="button"
            className="btn btn-neutral"
          >
            <FaPlus />
            Criar nova etiqueta
          </button>
        </div>
        <p className="-mt-1 mb-2 font-thin">
          Clique para marcar ou desmarcar uma etiqueta.
        </p>
        <ul className="grid grid-cols-1 sm:grid-cols-3 md:grid-cols-4 gap-4">
          {queryCandidates.data.items.map((tag) => {
            return (
              <TagCandidateItem
                disabled={isPending}
                onUpdate={(content) => {
                  setIsLoading(true);
                  updateMutation.mutate({
                    id: tag.id,
                    content,
                  });
                }}
                onDelete={() => {
                  setIsLoading(true);
                  deleteMutation.mutate(tag.id);
                }}
                onClick={() => {
                  setIsLoading(true);
                  if (tag.used) deselectMutation.mutate(tag.id);
                  else selectMutation.mutate(tag.id);
                }}
                key={`${project.team}-tags-${tag.id}`}
                tag={tag}
              />
            );
          })}
          {queryCandidates.data.items.length < 8 &&
            Array.from({ length: 8 - queryCandidates.data.items.length }).map(
              (_, i) => (
                <li
                  className="w-full"
                  key={`candidates-${
                    // biome-ignore lint/suspicious/noArrayIndexKey: not needed
                    i
                  }`}
                  tabIndex={-1}
                >
                  <div
                    className={clsx(
                      "bg-base-100 h-10 flex flex-row",
                      "items-center justify-start",
                      "rounded-md p-4 gap-4 relative w-full",
                    )}
                  />
                </li>
              ),
            )}
        </ul>
        <Pagination
          className="outline outline-2 rounded-lg outline-base-100"
          current={page}
          last={queryCandidates.data.lastPage}
          onChange={setPage}
        />
      </div>
    </>
  );
}
