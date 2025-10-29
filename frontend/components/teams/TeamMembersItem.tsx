"use client";

import clsx from "clsx";
import { Participation, Role } from "@/services/participations";
import Avatar from "../Avatar";
import { useEffect, useState } from "react";
import { Api } from "@/services/api";
import { ImageData } from "@/services/image";
import {
  FaCrow,
  FaCrown,
  FaGear,
  FaIdBadge,
  FaShield,
  FaShieldHalved,
  FaUserShield,
} from "react-icons/fa6";
import { Team } from "@/services/teams";
import TeamMemberContextMenu from "./TeamMemberContextMenu";

interface Props {
  team: Team;
  participation: Participation;
  viewer: Participation;
}

const icons = {
  OWNER: <FaCrown className="size-5 mb-0.5 text-warning" />,
  ADMIN: <FaShieldHalved className="size-5 mb-0.5 text-neutral" />,
  MEMBER: undefined,
};

export default function TeamMemberItem({ team, viewer, participation }: Props) {
  const [avatar, setAvatar] = useState<ImageData>();

  useEffect(() => {
    Api.client()
      .images()
      .urlToData(`/avatars/${participation.account.id}.webp`)
      .then((res) => setAvatar(res))
      .catch(() => {});
  }, [participation.account.id]);

  // TODO - Tem que ver a questão dos nome grande demais,
  // sugestão de solução, colocar [...] no final.
  // Por exemplo: Lucas Marcel Sil...
  // Quebra de linha não parece legal.
  if ((null !== participation.account.name) && participation.account.name.length>=33) {
    participation.account.name = participation.account.name.substring(0,30).concat("...");
  }

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
            avatar={avatar}
          />
        </div>
        <div className="flex flex-col justify-start items-start flex-1">
          <div className="flex flex-row gap-1.5 items-center justify-start">
            {icons[participation.role]}
            <h3 className="text-lg font-semibold">
              {participation.account.name}
            </h3>
          </div>
          <p className="font-light -mt-1 text-start">
            {participation.account.email}
          </p>
        </div>
        <TeamMemberContextMenu viewer={viewer} participation={participation} />
      </div>
    </li>
  );
}
