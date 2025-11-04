"use client";

import clsx from "clsx";
import { redirect } from "next/navigation";
import { type ChangeEvent, type FormEvent, useState } from "react";
import {
  FaDungeon,
  FaHashtag,
  FaPersonRunning,
  FaTriangleExclamation,
} from "react-icons/fa6";
import Input from "@/components/Input";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";

export default function CreateTeamPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>("");
  const [code, setCode] = useState<string>("");

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");

    if (!code.trim()) {
      setError("time não encontrado");
      return;
    }

    setIsLoading(true);
    const team = await Api.client()
      .teams()
      .join(code)
      .catch((err: ApiErrorResponse) => {
        if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return undefined;
      })
      .finally(() => setIsLoading(false));

    if (team) redirect(`/home/teams/${team.id}`);
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setCode(e.target.value);

  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-center justify-center sm:flex-1 not-sm:bg-base-200",
        "not-sm:justify-start",
      )}
    >
      <h1
        className={clsx(
          "text-2xl font-semibold not-sm:py-4",
          "not-sm:text-xl not-sm:self-start",
          "not-sm:px-4 bg-base-100 not-sm:w-full",
          "flex flex-row items-center gap-2",
        )}
      >
        <FaDungeon className="size-6" />
        Entrar por código
      </h1>
      <form
        onSubmit={onSubmit}
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full not-sm:rounded-none sm:w-xs border",
          "p-4 m-4 not-sm:m-0",
        )}
      >
        <Input
          name="code"
          type="text"
          placeholder="Código"
          icon={FaHashtag}
          value={code}
          onChange={onChange}
          error={error}
          hiddenError={!!error}
          disabled={isLoading}
        />
        <button disabled={isLoading} type="submit" className="btn btn-neutral">
          <FaPersonRunning />
          Entrar
        </button>
      </form>
      {error && (
        <div
          role="alert"
          className={clsx(
            "alert alert-error alert-soft w-xs",
            "not-sm:w-full not-sm:rounded-none",
          )}
        >
          <FaTriangleExclamation className="size-4 -mr-1" />
          <span className="first-letter:uppercase">{error}</span>
        </div>
      )}
    </main>
  );
}
