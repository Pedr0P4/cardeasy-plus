"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { FaCrown, FaShieldHalved } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";
import Avatar from "../Avatar";
import TeamMemberContextMenu from "./TeamMemberContextMenu";

interface Props {
  participation: Participation;
  viewer: Participation;
}

const icons = {
  OWNER: <FaCrown className="size-5 mb-0.5 text-warning" />,
  ADMIN: <FaShieldHalved className="size-5 mb-0.5 text-neutral" />,
  MEMBER: undefined,
};

export default function TeamMemberItem({ viewer, participation }: Props) {
  const query = useQuery({
    queryKey: [
      "participations",
      participation.team.id,
      "account",
      participation.account.id,
      "avatar",
    ],
    queryFn: () =>
      Api.client()
        .images()
        .urlToData(`/avatars/${participation.account.id}.webp`),
    retry: false,
  });

  return (
    <li className="w-full" tabIndex={-1}>
      <div
        className={clsx(
          "bg-base-200 h-22 flex flex-row",
          "items-center justify-start",
          "rounded-md px-6 py-4 gap-4 relative",
        )}
      >
        <div className="not-md:hidden">
          <Avatar
            className="!size-8"
            name={participation.account.name ?? ""}
            avatar={query.data}
          />
        </div>
        <div className="flex flex-col justify-start items-start flex-1 overflow-hidden">
          <div className="flex flex-row gap-1.5 items-center justify-start w-full">
            {icons[participation.role]}
            <h3 className="text-lg font-semibold truncate w-full pr-8 text-start">
              {participation.account.name}
            </h3>
          </div>
          <p className="font-light -mt-1 text-start truncate w-full pr-8 text-start">
            {participation.account.email}
          </p>
        </div>
        <TeamMemberContextMenu viewer={viewer} participation={participation} />
      </div>
    </li>
  );
}
