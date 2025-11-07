"use client";

import { useInfiniteQuery } from "@tanstack/react-query";
import { useState } from "react";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { Api } from "@/services/api";
import Input from "../Input";
import TeamItem from "./TeamItem";

export default function Teams() {
  const [searchQuery, setSearchQuery] = useState("");

  const query = useInfiniteQuery({
    queryKey: ["participations", `query-${searchQuery}`],
    queryFn: ({ pageParam }) =>
      Api.client().participations().search(pageParam, searchQuery),
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
        placeholder="Pesquisar por título ou descrição"
        label="Pesquisar por título ou descrição"
        icon={FaMagnifyingGlass}
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data.map((participation) => (
          <TeamItem key={participation.team.id} participation={participation} />
        ))}
      </ul>
    </>
  );
}
