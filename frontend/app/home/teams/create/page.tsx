"use client";

import Input from "@/components/Input";
import { Api } from "@/services/api";
import { ApiErrorResponse } from "@/services/base/axios";
import { CreateTeamData } from "@/services/teams";
import clsx from "clsx";
import { redirect } from "next/navigation";
import { ChangeEvent, FormEvent, useState } from "react";
import {
  FaClipboardList,
  FaPencil,
  FaPenClip,
  FaTriangleExclamation,
} from "react-icons/fa6";

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
        <button type="submit" className="btn btn-neutral mt-2">
          <FaPencil />
          Registrar-se
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
