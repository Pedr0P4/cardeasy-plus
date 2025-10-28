"use client";

import { Api } from "@/services/api";
import { ApiErrorResponse } from "@/services/base/axios";
import { Role, Team, UpdateTeamData } from "@/services/teams";
import clsx from "clsx";
import { UUID } from "crypto";
import { redirect } from "next/navigation";
import { ChangeEvent, FormEvent, useState } from "react";
import Input from "../Input";
import {
  FaClipboardList,
  FaFloppyDisk,
  FaGear,
  FaPenClip,
  FaTrash,
  FaTriangleExclamation,
} from "react-icons/fa6";

interface Props {
  team: Team;
  role: Role;
}

export default function TeamConfiguration({ team, role }: Props) {
  const isOwner = role === Role.OWNER;

  // TODO - Adicionar gerenciamento de convite
  // TODO - Melhorar responsividade

  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<UpdateTeamData>({
    title: team.title,
    description: team.description,
  });

  const onDeleteTeam = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    const success = await Api.client()
      .teams()
      .delete(team.id as UUID)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isValidationError()) setErrors(err.errors);
        else if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect("/home");
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    const success = await Api.client()
      .teams()
      .update(team.id as UUID, data)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isValidationError()) setErrors(err.errors);
        else if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect(`/home/teams/${team.id}`);
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
        "not-sm:justify-start sm:py-6",
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
        <FaGear className="size-8" />
        Configurações
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
          <FaFloppyDisk />
          Salvar alterações
        </button>
        {isOwner && (
          <button
            onClick={onDeleteTeam}
            type="button"
            className="btn btn-primary"
          >
            <FaTrash />
            Apagar time
          </button>
        )}
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
