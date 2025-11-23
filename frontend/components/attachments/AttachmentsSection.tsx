"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useState } from "react";
import { FaMagnifyingGlass, FaPaperclip, FaPlus } from "react-icons/fa6";
import Input from "@/components/Input";
import Pagination from "@/components/Pagination";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import AttachmentItem from "./AttachmentItem";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function AttachmentsSection({ project, cardList, card }: Props) {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const query = useQuery({
    queryKey: [
      "projects",
      project.id,
      "cards-lists",
      cardList.id,
      "cards",
      card.id,
      "attachments",
      `page-${page}`,
      `query-${searchQuery}`,
    ],
    queryFn: () =>
      Api.client().attachments().search(card.id, page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
      total: 0,
    },
  });

  const isPending = isLoading;

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
        <FaPaperclip className="size-6" />
        Anexos
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
            placeholder="Pesquisar por nome do arquivo"
            icon={FaMagnifyingGlass}
            value={searchQuery}
            onChange={(e) => {
              setSearchQuery(e.target.value);
              setPage(0);
            }}
          />
          <Link
            className="btn btn-neutral"
            onClick={(e) => e.currentTarget.blur()}
            href={`/home/teams/${project.team}/projects/${project.id}/card-lists/${cardList.id}/cards/${card.id}/attachments/create`}
          >
            <FaPlus />
            Criar novo anexo
          </Link>
        </div>
        <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {query.data.items.map((attachment) => {
            return (
              <AttachmentItem
                card={card}
                cardList={cardList}
                project={project}
                disabled={isPending}
                onClick={() => {
                  setIsLoading(true);
                }}
                key={`${card.id}-attachment-${attachment.id}`}
                attachment={attachment}
              />
            );
          })}
          {query.data.items.length < 6 &&
            Array.from({ length: 6 - query.data.items.length }).map((_, i) => (
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
                    "bg-base-100 h-18 flex flex-row",
                    "items-center justify-start",
                    "rounded-md px-5 py-4 gap-4 relative w-full",
                  )}
                />
              </li>
            ))}
        </ul>
        <Pagination
          current={page}
          last={query.data.lastPage}
          onChange={setPage}
        />
      </div>
    </>
  );
}
