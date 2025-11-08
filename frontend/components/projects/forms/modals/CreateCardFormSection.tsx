"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { type ChangeEvent, useState } from "react";
import {
  FaClipboardList,
  FaPenClip,
  FaPencil,
  FaPlus,
  FaTriangleExclamation,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CardList } from "@/services/cardLists";
import type { CreateCardData } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Toasts } from "@/services/toats";
import Input from "../../../Input";

interface Props {
  project: Project;
  cardList: CardList;
}

export default function CreateCardFormSection({ project, cardList }: Props) {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<CreateCardData>({
    cardList: cardList.id,
    title: "",
    description: "",
  });

  const router = useRouter();
  const queryClient = useQueryClient();
  const createMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .cards()
        .create(data)
        .then(() => {
          Toasts.success("Cartão criado com sucesso!");

          queryClient.invalidateQueries({
            queryKey: ["projects", project.id, "cards-lists"],
          });

          router.push(`/home/teams/${project.team}/projects/${project.id}`);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
        })
        .finally(() => setIsLoading(false));
    },
  });

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const isPending = isLoading || createMutation.isPending;

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
        <FaPlus className="size-6" />
        Criar novo cartão
      </h1>
      <div
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full rounded-none border",
          "py-4 px-4 sm:px-6  m-0",
        )}
      >
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            setError("");
            setErrors({});
            setIsLoading(true);
            createMutation.mutate();
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
            <button
              disabled={isPending}
              type="submit"
              className="btn btn-neutral"
            >
              <FaPencil />
              Criar
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
