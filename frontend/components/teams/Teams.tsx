"use client";

import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useState } from "react";
import { FaDungeon, FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import Input from "../Input";
import Pagination from "../Pagination";
import TeamItem from "./TeamItem";

export default function Teams() {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");

  const query = useQuery({
    queryKey: ["teams", `page-${page}`, `query-${searchQuery}`],
    queryFn: () => Api.client().teams().search(page, searchQuery),
    initialData: {
      items: [],
      page,
      lastPage: -1,
      total: 0,
    },
  });

  return (
    <>
      <div className="flex flex-col md:flex-row gap-4 mb-2 md:items-end">
        <Link href="/home/teams/create" className="btn btn-neutral">
          <FaPlus />
          Criar novo time
        </Link>
        <Link href="/home/teams/join" className="btn btn">
          <FaDungeon />
          Entrar por código
        </Link>
        <Input
          name="search"
          type="text"
          placeholder="Pesquisar por título ou descrição"
          icon={FaMagnifyingGlass}
          value={searchQuery}
          onChange={(e) => {
            setSearchQuery(e.target.value);
            setPage(0);
          }}
        />
      </div>
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {query.data.items.map((team) => (
          <TeamItem key={`team-${team.id}`} team={team} />
        ))}
      </ul>
      <Pagination
        current={page}
        last={query.data.lastPage}
        onChange={setPage}
      />
    </>
  );
}
