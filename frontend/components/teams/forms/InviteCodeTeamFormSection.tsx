"use client";

import clsx from "clsx";
import type { UUID } from "crypto";
import { type FormEvent, useState } from "react";
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
import Input from "../../Input";

interface Props {
  team: Team;
}

export default function InviteCodeTeamFormSection({ team }: Props) {
  const [error, setError] = useState<string>("");
  const [code, setCode] = useState<string>(team?.code ?? "");

  const onDeleteCode = async () => {
    setError("");

    const success = await Api.client()
      .teams()
      .deleteCode(team.id as UUID)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) setCode("");
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");

    const code = await Api.client()
      .teams()
      .generateCode(team.id as UUID)
      .catch((err: ApiErrorResponse) => {
        if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return "";
      });

    if (code) setCode(code);
  };

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
          onSubmit={onSubmit}
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
          />
          <div className="flex flex-row flex-wrap gap-4">
            <button type="submit" className="btn btn-neutral">
              <FaArrowsRotate />
              Gerar código
            </button>
            <button
              onClick={onDeleteCode}
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
