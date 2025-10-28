"use client";

import clsx from "clsx";
import type { Participation, Role } from "@/services/participations";
import Avatar from "../Avatar";
import { useEffect, useState } from "react";
import { Api } from "@/services/api";
import { ImageData } from "@/services/image";
import {
  FaCrow,
  FaCrown,
  FaIdBadge,
  FaShield,
  FaShieldHalved,
  FaUserShield,
} from "react-icons/fa6";
import { Team } from "@/services/teams";

interface Props {
  team: Team;
  participation: Participation;
  role: Role;
}

const icons = {
  OWNER: <FaCrown className="size-5 mb-0.5 text-warning" />,
  ADMIN: <FaShieldHalved className="size-5 mb-0.5 text-neutral" />,
  MEMBER: undefined,
};

export default function TeamMemberItem({ team, participation }: Props) {
  const [avatar, setAvatar] = useState<ImageData>();

  useEffect(() => {
    Api.client()
      .images()
      .urlToData(`/avatars/${participation.account.id}.webp`)
      .then((res) => setAvatar(res))
      .catch(() => {});
  }, [participation.account.id]);

  // TODO - Expulsar alguém
  // TODO - Questão do nome grande demais

  return (
    <li className="w-full" tabIndex={-1}>
      <div
        className={clsx(
          "bg-base-200 h-22 flex flex-row",
          "items-center justify-start",
          "rounded-md px-6 py-4 gap-4",
        )}
      >
        <div className="not-md:hidden">
          <Avatar
            className="!size-8"
            name={participation.account.name ?? ""}
            avatar={avatar}
          />
        </div>
        <div className="flex flex-col justify-start items-start">
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
      </div>
    </li>
  );
}
