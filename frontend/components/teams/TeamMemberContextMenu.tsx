"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { useState } from "react";
import {
  FaArrowDown,
  FaArrowRightArrowLeft,
  FaArrowUp,
  FaDoorOpen,
  FaGavel,
  FaGear,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import { type Participation, Role } from "@/services/participations";
import { Toasts } from "@/services/toats";

interface Props {
  viewer: Participation;
  participation: Participation;
}

const levels = {
  OWNER: 2,
  ADMIN: 1,
  MEMBER: 0,
};

export default function TeamMemberContextMenu({
  viewer,
  participation,
}: Props) {
  const isOnwer = viewer.role === Role.OWNER;
  const same = viewer.account.id === participation.account.id;
  const level = levels[viewer.role] - levels[participation.role];
  const [isLoading, setIsLoading] = useState(false);

  const router = useRouter();
  const queryClient = useQueryClient();
  const promoteToAdminMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .participations()
        .update({
          account: participation.account.id,
          team: participation.team.id,
          role: Role.ADMIN,
        })
        .then(() => {
          Toasts.success("Membro promovido com sucesso!");
          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id],
          });
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const demoteToMemberMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .participations()
        .update({
          account: participation.account.id,
          team: participation.team.id,
          role: Role.MEMBER,
        })
        .then(() => {
          Toasts.success("Membro rebaixado com sucesso!");
          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id],
          });
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const transferOwnershipMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .teams()
        .transfer(participation.team.id, participation.account.id)
        .then(() => {
          Toasts.success("Posse do time transferida com sucesso!");

          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id],
          });
          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id, "me"],
          });
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const kickMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .participations()
        .delete({
          account: participation.account.id,
          team: participation.team.id,
        })
        .then(() => {
          Toasts.success("Membro expulso do time com sucesso!");
          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id],
          });
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const exitMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .participations()
        .exit({
          team: viewer.team.id,
        })
        .then(() => {
          Toasts.success("Você saiu do time com sucesso!");
          queryClient.removeQueries({
            queryKey: ["participations", participation.team.id],
          });
          queryClient.removeQueries({
            queryKey: ["participations", participation.team.id, "me"],
          });
          router.push("/home");
        })
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const isPending =
    isLoading ||
    exitMutation.isPending ||
    kickMutation.isPending ||
    transferOwnershipMutation.isPending ||
    promoteToAdminMutation.isPending ||
    demoteToMemberMutation.isPending;

  if (level <= 0 && (!same || (same && isOnwer))) return null;

  return (
    <div className="absolute top-0 right-0 dropdown dropdown-end">
      <button tabIndex={0} type="button" className="btn m-1">
        <FaGear />
      </button>
      <ul
        tabIndex={-1}
        className={clsx(
          "dropdown-content menu bg-base-100",
          "rounded-box z-1 w-64 p-2 shadow-sm",
          "border border-base-content",
        )}
      >
        {isOnwer && !same && (
          <>
            {level >= 2 ? (
              <li>
                <button
                  type="button"
                  disabled={isPending}
                  onClick={(e) => {
                    e.currentTarget.blur();
                    setIsLoading(true);
                    promoteToAdminMutation.mutate();
                  }}
                >
                  <FaArrowUp />
                  Promover à administrador
                </button>
              </li>
            ) : (
              <li>
                <button
                  type="button"
                  disabled={isPending}
                  onClick={(e) => {
                    e.currentTarget.blur();
                    setIsLoading(true);
                    demoteToMemberMutation.mutate();
                  }}
                >
                  <FaArrowDown />
                  Rebaixar à membro
                </button>
              </li>
            )}
            <li>
              <button
                type="button"
                disabled={isPending}
                onClick={(e) => {
                  e.currentTarget.blur();
                  setIsLoading(true);
                  transferOwnershipMutation.mutate();
                }}
                className="text-primary"
              >
                <FaArrowRightArrowLeft />
                Tramsferir pose
              </button>
            </li>
          </>
        )}
        {level >= 1 && !same && (
          <li>
            <button
              type="button"
              disabled={isPending}
              onClick={(e) => {
                e.currentTarget.blur();
                setIsLoading(true);
                kickMutation.mutate();
              }}
              className="text-primary"
            >
              <FaGavel />
              Expulsar do time
            </button>
          </li>
        )}
        {same && !isOnwer && (
          <li>
            <button
              type="button"
              disabled={isPending}
              onClick={(e) => {
                e.currentTarget.blur();
                setIsLoading(true);
                exitMutation.mutate();
              }}
              className="text-primary"
            >
              <FaDoorOpen />
              Sair do time
            </button>
          </li>
        )}
      </ul>
    </div>
  );
}
