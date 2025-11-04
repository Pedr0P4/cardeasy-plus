"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
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

  // TODO - Toast?
  // TODO - Rever transição de posse

  const router = useRouter();
  const queryClient = useQueryClient();
  const promoteToAdminMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .participations()
        .update({
          accountId: participation.account.id,
          teamId: participation.team.id,
          role: Role.ADMIN,
        })
        .catch((err: ApiErrorResponse) => {
          // if (err.isValidationError()) setErrors(err.errors);
          // else if (err.isErrorResponse()) setError(err.error);
          // else setError("Erro inesperado!");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["participations", participation.team.id],
      });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const demoteToMemberMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .participations()
        .update({
          accountId: participation.account.id,
          teamId: participation.team.id,
          role: Role.MEMBER,
        })
        .catch((err: ApiErrorResponse) => {
          // if (err.isValidationError()) setErrors(err.errors);
          // else if (err.isErrorResponse()) setError(err.error);
          // else setError("Erro inesperado!");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["participations", participation.team.id],
      });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const transferOwnershipMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .participations()
        .update({
          accountId: participation.account.id,
          teamId: participation.team.id,
          role: viewer.role,
        })
        .catch((err: ApiErrorResponse) => {
          // if (err.isValidationError()) setErrors(err.errors);
          // else if (err.isErrorResponse()) setError(err.error);
          // else setError("Erro inesperado!");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["participations", participation.team.id],
      });
      queryClient.invalidateQueries({
        queryKey: ["participations", participation.team.id, "me"],
      });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const kickMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .participations()
        .delete({
          accountId: participation.account.id,
          teamId: participation.team.id,
        })
        .catch((err: ApiErrorResponse) => {
          // if (err.isValidationError()) setErrors(err.errors);
          // else if (err.isErrorResponse()) setError(err.error);
          // else setError("Erro inesperado!");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["participations", participation.team.id],
      });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const exitMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .participations()
        .delete({
          accountId: viewer.account.id,
          teamId: viewer.team.id,
        })
        .catch((err: ApiErrorResponse) => {
          // if (err.isValidationError()) setErrors(err.errors);
          // else if (err.isErrorResponse()) setError(err.error);
          // else setError("Erro inesperado!");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.removeQueries({
        queryKey: ["participations", participation.team.id],
      });
      queryClient.removeQueries({
        queryKey: ["participations", participation.team.id, "me"],
      });
      router.push("/home");
    },
    onError: (error) => {
      console.log(error);
    },
  });

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
                  onClick={(e) => {
                    e.currentTarget.blur();
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
                  onClick={(e) => {
                    e.currentTarget.blur();
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
                onClick={(e) => {
                  e.currentTarget.blur();
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
              onClick={(e) => {
                e.currentTarget.blur();
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
              onClick={(e) => {
                e.currentTarget.blur();
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
