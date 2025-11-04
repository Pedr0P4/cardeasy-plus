"use client";

import clsx from "clsx";
import { useEffect, useState } from "react";
import { FaCrown, FaShieldHalved } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ImageData } from "@/services/image";
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
  const [avatar, setAvatar] = useState<ImageData>();

  useEffect(() => {
    Api.client()
      .images()
      .urlToData(`/avatars/${participation.account.id}.webp`)
      .then((res) => setAvatar(res))
      .catch(() => {});
  }, [participation.account.id]);

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
