import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Image from "next/image";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";

interface Props {
  participation: Participation;
}

export default function AssingmentsAvatar({ participation }: Props) {
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
            {participation.account.name.length > 0
              ? participation.account.name[0].toUpperCase()
              : "U"}
          </span>
        )}
      </div>
    </div>
  );
}
