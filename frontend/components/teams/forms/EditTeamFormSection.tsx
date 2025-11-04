"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import {
  type ChangeEvent,
  type Dispatch,
  type SetStateAction,
  useState,
} from "react";
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
import { Role } from "@/services/participations";
import type { Team, UpdateTeamData } from "@/services/teams";
import Input from "../../Input";

interface Props {
  team: Team;
  role: Role;
  isLoading: boolean;
  setIsLoading: Dispatch<SetStateAction<boolean>>;
}

export default function EditTeamFormSection({
  team,
  role,
  isLoading,
  setIsLoading,
}: Props) {
  const isOwner = role === Role.OWNER;

  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<UpdateTeamData>({
    title: team.title,
    description: team.description,
  });

  const router = useRouter();
  const queryClient = useQueryClient();
  const deleteMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .teams()
        .delete(team.id)
        .then(() => {
          queryClient.invalidateQueries({ queryKey: ["participations"] });
          queryClient.removeQueries({ queryKey: ["participations", team.id] });
          queryClient.removeQueries({
            queryKey: ["participations", team.id, "me"],
          });
          router.push("/home");
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        })
        .finally(() => setIsLoading(false));
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const updateMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .teams()
        .update(team.id, data)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["participations", team.id],
          });
          router.push(`/home/teams/${team.id}`);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        })
        .finally(() => setIsLoading(false));
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const isPending =
    isLoading || updateMutation.isPending || deleteMutation.isPending;

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
          onSubmit={(e) => {
            e.preventDefault();
            setError("");
            setErrors({});

            updateMutation.mutate();
          }}
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
            disabled={isPending}
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
            disabled={isPending}
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
            {isOwner && (
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
