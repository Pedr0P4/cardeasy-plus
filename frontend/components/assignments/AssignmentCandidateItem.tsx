"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { FaCrown, FaShieldHalved } from "react-icons/fa6";
import Avatar from "@/components/Avatar";
import { Api } from "@/services/api";
import type { AssignmentCandidate } from "@/services/assignments";

interface Props {
  assignment: AssignmentCandidate;
  disabled?: boolean;
  onClick: () => void;
}

const icons = {
  OWNER: <FaCrown className="size-5 mb-0.5 text-warning" />,
  ADMIN: <FaShieldHalved className="size-5 mb-0.5 text-neutral" />,
  MEMBER: undefined,
};

export default function AssignmentCandidateItem({
  assignment,
  onClick,
  disabled = false,
}: Props) {
  const query = useQuery({
    queryKey: [
      "participations",
      assignment.team,
      "account",
      assignment.account,
      "avatar",
    ],
    queryFn: () =>
      Api.client().images().urlToData(`/avatars/${assignment.account}.webp`),
    retry: false,
  });

  return (
    <li className="w-full" tabIndex={-1}>
      <button
        type="button"
        disabled={disabled}
        onClick={onClick}
        className={clsx(
          "btn bg-base-300 h-22 flex flex-row",
          "items-center justify-start",
          "rounded-md px-6 py-4 gap-4 relative w-full",
          assignment.assigned && "btn-neutral border-2",
        )}
      >
        <div className="not-md:hidden">
          <Avatar
            className="!size-8"
            name={assignment.name ?? ""}
            avatar={query.data}
          />
        </div>
        <div className="flex flex-col justify-start items-start flex-1 overflow-hidden">
          <div className="flex flex-row gap-1.5 items-center justify-start w-full">
            {icons[assignment.role]}
            <h3 className="text-lg font-semibold truncate w-full pr-8 text-start">
              {assignment.name}
            </h3>
          </div>
          <p className="font-light -mt-1 text-start truncate w-full pr-8 text-start">
            {assignment.email}
          </p>
        </div>
      </button>
    </li>
  );
}
