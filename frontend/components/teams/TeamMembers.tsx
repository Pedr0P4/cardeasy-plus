"use client";

import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";
import Input from "../Input";
import Pagination from "../Pagination";
import TeamMemberItem from "./TeamMembersItem";

interface Props {
  participation: Participation;
}

export default function TeamMembers({ participation }: Props) {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");

  const participationQuery = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: async () =>
      Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  const query = useQuery({
    queryKey: [
      "participations",
      participation.team.id,
      `page-${page}`,
      `query-${searchQuery}`,
    ],
    queryFn: () =>
      Api.client()
        .teams()
        .participations(participation.team.id, page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
    },
  });

  return (
    <>
      <Input
        name="search"
        type="text"
        className="mb-4"
        placeholder="Pesquisar por nome ou email"
        icon={FaMagnifyingGlass}
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data.items.map((participation) => {
          return (
            <TeamMemberItem
              key={`${participation.team.id}-${participation.account.id}`}
              viewer={participationQuery.data}
              participation={participation}
            />
          );
        })}
      </ul>
      <Pagination
        current={page}
        last={query.data.lastPage}
        onChange={setPage}
      />
    </>
  );
}
