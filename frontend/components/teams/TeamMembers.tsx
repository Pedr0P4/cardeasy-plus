"use client";

import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { Api } from "@/services/api";
import { type Participation, Role } from "@/services/participations";
import Input from "../Input";
import TeamMemberItem from "./TeamMembersItem";

interface Props {
  participation: Participation;
}

const roles = [Role.OWNER, Role.ADMIN, Role.MEMBER];

export default function TeamMembers({ participation }: Props) {
  const [searchQuery, setSearchQuery] = useState("");

  const participationQuery = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: async () =>
      Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  const query = useInfiniteQuery({
    queryKey: ["participations", participation.team.id, `query-${searchQuery}`],
    queryFn: ({ pageParam }) =>
      Api.client()
        .teams()
        .participations(participation.team.id, pageParam, searchQuery),
    getNextPageParam: (lastPageData) => {
      if (lastPageData.page < lastPageData.lastPage) {
        return lastPageData.page + 1;
      }
      return undefined;
    },
    select: (data) => {
      return data.pages.flatMap((page) => page.items);
    },
    initialPageParam: 0,
    initialData: {
      pages: [],
      pageParams: [],
    },
  });

  return (
    <>
      <Input
        name="title"
        type="text"
        className="mb-4"
        placeholder="Pesquisar por nome ou email"
        label="Pesquisar por nome ou email"
        icon={FaMagnifyingGlass}
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data
          .sort((a, b) => roles.indexOf(a.role) - roles.indexOf(b.role))
          .map((participation) => {
            return (
              <TeamMemberItem
                key={`${participation.team.id}-${participation.account.id}`}
                viewer={participationQuery.data}
                participation={participation}
              />
            );
          })}
      </ul>
    </>
  );
}
