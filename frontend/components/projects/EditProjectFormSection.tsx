"use client";

import { Api } from "@/services/api";
import { ApiErrorResponse } from "@/services/base/axios";
import { Role } from "@/services/teams";
import clsx from "clsx";
import { redirect } from "next/navigation";
import { ChangeEvent, FormEvent, useState } from "react";
import {
  FaClipboardList,
  FaFloppyDisk,
  FaPenClip,
  FaPenRuler,
  FaTrash,
  FaTriangleExclamation,
} from "react-icons/fa6";
import Input from "../Input";
import { Project, UpdateProjectData } from "@/services/projects";

interface Props {
  project: Project;
  role: Role;
}

export default function EditProjectFormSection({ project, role }: Props) {
  const isOwner = role === Role.OWNER;

  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<UpdateProjectData>({
    title: project.title,
    description: project.description,
  });

  const onDeleteProject = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    const success = await Api.client()
      .projects()
      .delete(project.id)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect(`/home/team/${project.team}`);
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    const success = await Api.client()
      .projects()
      .update(project.id, data)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isValidationError()) setErrors(err.errors);
        else if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect(`/home/teams/${project.team}/projects/${project.id}`);
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
        <FaPenRuler className="size-6" />
        Editar projeto
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
                onClick={onDeleteProject}
                type="button"
                className="btn btn-soft btn-primary"
              >
                <FaTrash />
                Apagar projeto
              </button>
            )}
          </div>
        </form>
      </div>
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
    </>
  );
}
