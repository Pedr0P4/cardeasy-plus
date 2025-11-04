"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { type ChangeEvent, useState } from "react";
import {
  FaCalendarDay,
  FaClipboardList,
  FaFloppyDisk,
  FaPenClip,
  FaPenRuler,
  FaTrash,
  FaTriangleExclamation,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { Project } from "@/services/projects";
import type { Stage, UpdateStageDTO } from "@/services/stages";
import Input from "../../../Input";

interface Props {
  project: Project;
  stage: Stage;
}

export default function EditStageFormSection({ project, stage }: Props) {
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [withExpectedEndIn, setWithExpectedEndIn] = useState(
    !!stage.expectedEndIn,
  );

  const [data, setData] = useState<UpdateStageDTO>({
    name: stage.name,
    state: stage.state,
    description: stage.description,
    expectedStartIn: stage.expectedStartIn,
    expectedEndIn: stage.expectedEndIn,
  });

  const router = useRouter();
  const queryClient = useQueryClient();
  const deleteMutation = useMutation({
    mutationFn: async () => {
      return Api.client().stages().delete(stage.id);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["projects", project.id, "stages"],
      });
      queryClient.invalidateQueries({ queryKey: ["projects", project.id] });
      router.push(`/home/teams/${project.team}/projects/${project.id}`);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const updateMutation = useMutation({
    mutationFn: async () => {
      return Api.client()
        .stages()
        .update(stage?.id, {
          ...data,
          expectedEndIn: withExpectedEndIn ? data.expectedEndIn : undefined,
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["projects", project.id, "stages"],
      });
      queryClient.invalidateQueries({ queryKey: ["projects", project.id] });
      router.push(`/home/teams/${project.team}/projects/${project.id}`);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const isPending = updateMutation.isPending || deleteMutation.isPending;

  const onChangeExpectedStartIn = (date?: Date) =>
    setData((data) => ({
      ...data,
      expectedStartIn: date ? date.getTime() : undefined,
    }));

  const onChangeExpectedEndIn = (date?: Date) =>
    setData((data) => ({
      ...data,
      expectedEndIn: date ? date.getTime() : undefined,
    }));

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const onChangeWithExpectedEndIn = (e: ChangeEvent<HTMLInputElement>) => {
    setWithExpectedEndIn(e.target.checked);
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
        <FaPenRuler className="size-6" />
        Editar etapa
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
            setError("");
            setErrors({});

            updateMutation.mutate();
          }}
          className={clsx("flex flex-col gap-4", "w-full sm:max-w-lg")}
        >
          <Input
            name="name"
            type="text"
            placeholder="Nome"
            label="Nome"
            icon={FaPenClip}
            value={data.name}
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
          <Input
            name="expectedStartIn"
            type="day"
            placeholder="Expectativa de início"
            label="Expectativa de início"
            icon={FaCalendarDay}
            selected={
              data.expectedStartIn ? new Date(data.expectedStartIn) : undefined
            }
            onSelect={onChangeExpectedStartIn}
            errors={errors}
            error={error}
            hiddenError={!!error}
          />
          <Input
            name="expectedEndIn"
            type="day"
            placeholder="Expectativa de termino"
            label="Expectativa de termino"
            onChangeOptional={onChangeWithExpectedEndIn}
            optional
            disabled={!withExpectedEndIn}
            icon={FaCalendarDay}
            selected={
              data.expectedEndIn ? new Date(data.expectedEndIn) : undefined
            }
            onSelect={onChangeExpectedEndIn}
            errors={errors}
            error={error}
            hiddenError={!!error}
          />
          <div className="flex flex-row flex-wrap gap-4">
            <button
              disabled={isPending}
              type="submit"
              className="btn btn-neutral"
            >
              <FaFloppyDisk />
              Salvar alterações
            </button>
            <button
              disabled={isPending}
              onClick={() => {
                setError("");
                setErrors({});
                deleteMutation.mutate();
              }}
              type="button"
              className="btn btn-soft btn-primary"
            >
              <FaTrash />
              Remover etapa
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
