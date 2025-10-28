"use client";

import clsx from "clsx";
import { redirect } from "next/navigation";
import { type ChangeEvent, type FormEvent, useState } from "react";
import {
  FaClipboardList,
  FaPenClip,
  FaPencil,
  FaPlus,
  FaTriangleExclamation,
} from "react-icons/fa6";
import Input from "@/components/Input";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CreateTeamData } from "@/services/teams";

export default function CreateTeamPage() {
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<CreateTeamData>({
    title: "",
    description: "",
  });

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    const team = await Api.client()
      .teams()
      .create(data)
      .catch((err: ApiErrorResponse) => {
        if (err.isValidationError()) setErrors(err.errors);
        else if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return undefined;
      });

    if (team) redirect(`/home/teams/${team.id}`);
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

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
        <FaPlus className="size-8" />
        Criar novo time
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
          name="title"
          type="text"
          placeholder="Título"
          label="Título"
          icon={FaPenClip}
          value={data.title}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <Input
          name="description"
          type="textarea"
          placeholder="Descrição"
          label="Descrição"
          className="min-h-32"
          icon={FaClipboardList}
          value={data.description}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <button type="submit" className="btn btn-neutral">
          <FaPencil />
          Criar
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
