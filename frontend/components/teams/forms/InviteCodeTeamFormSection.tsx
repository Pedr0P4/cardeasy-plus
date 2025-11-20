"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import type { UUID } from "crypto";
import { type Dispatch, type SetStateAction, useState } from "react";
import {
  FaArrowsRotate,
  FaEnvelopeOpenText,
  FaHashtag,
  FaTrash,
  FaTriangleExclamation,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { Team } from "@/services/teams";
import { Toasts } from "@/services/toats";
import Input from "../../Input";

interface Props {
  team: Team;
  isLoading: boolean;
  setIsLoading: Dispatch<SetStateAction<boolean>>;
}

export default function InviteCodeTeamFormSection({
  team,
  isLoading,
  setIsLoading,
}: Props) {
  const [error, setError] = useState<string>("");
  const [code, setCode] = useState<string>(team?.code ?? "");

  const queryClient = useQueryClient();
  const deleteCodeMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .teams()
        .deleteCode(team.id as UUID)
        .then(() => {
          Toasts.success("Codigo apagado com sucesso!");
          queryClient.invalidateQueries({
            queryKey: ["participations", team.id],
          });
          setCode("");
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const generateCodeMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .teams()
        .generateCode(team.id as UUID)
        .then((code) => {
          Toasts.success("Codigo gerado com sucesso!");
          queryClient.invalidateQueries({
            queryKey: ["participations", team.id],
          });
          setCode(code);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const isPending =
    isLoading || deleteCodeMutation.isPending || generateCodeMutation.isPending;

  return (
    <>
      <h1
        className={clsx(
          "text-2xl font-semibold",
          "text-xl self-start",
          "py-4 px-4 sm:px-6  bg-base-100 w-full",
          "flex flex-row items-center gap-2",
        )}
      >
        <FaEnvelopeOpenText className="size-6" />
        Código de convite
      </h1>
      <div
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full rounded-none border",
          "py-4 px-4 sm:px-6  m-0",
        )}
      >
        <form
          onSubmit={(e) => {
            e.preventDefault();
            generateCodeMutation.mutate();
          }}
          className={clsx("flex flex-col gap-4", "w-full sm:max-w-lg")}
        >
          <Input
            name="code"
            type="text"
            placeholder="Código desativado"
            label="Código de convite"
            readOnly
            icon={FaHashtag}
            value={code}
            error={error}
            hiddenError={!!error}
            disabled={isPending}
          />
          <div className="flex flex-row flex-wrap gap-4">
            <button
              disabled={isPending}
              type="submit"
              className="btn btn-neutral"
            >
              <FaArrowsRotate />
              Gerar código
            </button>
            <button
              disabled={isPending}
              onClick={() => {
                setError("");
                deleteCodeMutation.mutate();
              }}
              type="button"
              className="btn btn-soft btn-primary"
            >
              <FaTrash />
              Remover código
            </button>
          </div>
        </form>
      </div>
      {error && (
        <div
          role="alert"
          className={clsx(
            "alert alert-error alert-soft",
            "w-full rounded-none sm:px-6",
          )}
        >
          <FaTriangleExclamation className="size-4 -mr-1" />
          <span className="first-letter:uppercase">{error}</span>
        </div>
      )}
    </>
  );
}
