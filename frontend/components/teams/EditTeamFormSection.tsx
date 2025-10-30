"use client";

import clsx from "clsx";
import type { UUID } from "crypto";
import { redirect } from "next/navigation";
import { type ChangeEvent, type FormEvent, useState } from "react";
import {
  FaClipboardList,
  FaFloppyDisk,
  FaPenClip,
  FaTrash,
  FaTriangleExclamation,
  FaUsersGear,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import { Role, type Team, type UpdateTeamData } from "@/services/teams";
import Input from "../Input";

interface Props {
  team: Team;
  role: Role;
}

export default function EditTeamFormSection({ team, role }: Props) {
  const isOwner = role === Role.OWNER;

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
        if (err.isErrorResponse()) setError(err.error);
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
    <>
      <h1
        className={clsx(
          "text-2xl font-semibold",
          "text-xl self-start",
          "py-4 px-4 sm:px-6  bg-base-100 w-full",
          "flex flex-row items-center gap-2",
        )}
      >
        <FaUsersGear className="size-6" />
        Editar time
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
          <div className="flex flex-row flex-wrap gap-4">
            <button type="submit" className="btn btn-neutral">
              <FaFloppyDisk />
              Salvar alterações
            </button>
            {isOwner && (
              <button
                onClick={onDeleteTeam}
                type="button"
                className="btn btn-soft btn-primary"
              >
                <FaTrash />
                Apagar time
              </button>
            )}
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
