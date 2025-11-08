import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Image from "next/image";
import { Api } from "@/services/api";
import type { Assignment } from "@/services/assignments";

interface Props {
  assignment: Assignment;
}

export default function AssingmentsAvatar({ assignment }: Props) {
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
    <div
      className={clsx(
        "relative avatar",
        !query.data && "avatar-placeholder",
        "border-none",
      )}
    >
      <div className="size-6 rounded-full bg-base-100">
        {query.data ? (
          <Image
            src={query.data?.url ?? ""}
            alt="user avatar"
            width={300}
            height={300}
          />
        ) : (
          <span className="text-sm">
            {assignment.name.length > 0
              ? assignment.name[0].toUpperCase()
              : "U"}
          </span>
        )}
      </div>
    </div>
  );
}
