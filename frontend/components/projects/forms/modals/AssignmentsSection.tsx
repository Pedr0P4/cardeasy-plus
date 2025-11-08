"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import type { UUID } from "crypto";
import { useState } from "react";
import {
  FaMagnifyingGlass,
  FaTriangleExclamation,
  FaUserGroup,
} from "react-icons/fa6";
import Input from "@/components/Input";
import Pagination from "@/components/Pagination";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import AssignmentCandidateItem from "./assignments/AssignmentCandidateItem";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function AssignmentsSection({ project, cardList, card }: Props) {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>("");

  // TODO - Toast?
  const queryCandidates = useQuery({
    queryKey: [
      "projects",
      project.id,
      "cards-lists",
      cardList.id,
      "cards",
      card.id,
      "assignments",
      "candidates",
      `page-${page}`,
      `query-${searchQuery}`,
    ],
    queryFn: () =>
      Api.client().assignments().searchCandidates(card.id, page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
      total: 0,
    },
  });

  const queryClient = useQueryClient();

  const selectMutation = useMutation({
    mutationFn: async (account: UUID) => {
      return await Api.client()
        .assignments()
        .create({ account, card: card.id })
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
              card.id,
              "assignments",
            ],
          });
        });
    },
    onError: (error) => {
      console.log(error);
      setIsLoading(false);
    },
  });

  const deselectMutation = useMutation({
    mutationFn: async (account: UUID) => {
      return await Api.client()
        .assignments()
        .delete({ account, card: card.id })
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
              card.id,
              "assignments",
            ],
          });
        });
    },
    onError: (error) => {
      console.log(error);
      setIsLoading(false);
    },
  });

  const isPending = isLoading || selectMutation.isPending;

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
        <FaUserGroup className="size-6" />
        Editar atribuições
      </h1>
      <div
        className={clsx(
          "flex flex-col gap-2 bg-base-200 border-base-300",
          "rounded-box w-full rounded-none border",
          "py-4 px-4 sm:px-6 m-0 pb-6",
        )}
      >
        <Input
          name="search"
          type="text"
          className="mb-2"
          placeholder="Pesquisar por nome ou email"
          icon={FaMagnifyingGlass}
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <p className="-mt-1 mb-2 font-thin">
          Clique para atribuir ou desfazer uma atribuição.
        </p>
        <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {queryCandidates.data.items.map((assignment) => {
            return (
              <AssignmentCandidateItem
                disabled={isPending}
                onClick={() => {
                  if (assignment.assigned)
                    deselectMutation.mutate(assignment.account);
                  else selectMutation.mutate(assignment.account);
                }}
                key={`${assignment.team}-${assignment.account}`}
                assignment={assignment}
              />
            );
          })}
          {queryCandidates.data.items.length < 6 &&
            Array.from({ length: 6 - queryCandidates.data.items.length }).map(
              (_, i) => (
                <li
                  className="w-full"
                  key={`candidates-${
                    // biome-ignore lint/suspicious/noArrayIndexKey: <explanation>
                    i
                  }`}
                  tabIndex={-1}
                >
                  <div
                    className={clsx(
                      "bg-base-100 h-22 flex flex-row",
                      "items-center justify-start",
                      "rounded-md px-6 py-4 gap-4 relative w-full",
                    )}
                  />
                </li>
              ),
            )}
        </ul>
        <Pagination
          current={0}
          last={queryCandidates.data.lastPage}
          onChange={setPage}
        />
      </div>
      {error && (
        <div
          role="alert"
          className={clsx(
            "alert alert-error alert-soft",
            "w-full rounded-none sm:px-6",
          )}
        >
          <FaTriangleExclamation className="size-4 -mr-1" />
          <span className="first-letter:uppercase">{error}</span>
        </div>
      )}
    </>
  );
}
