"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { type ChangeEvent, useState } from "react";
import {
  FaFilePdf,
  FaPencil,
  FaPlus,
  FaTriangleExclamation,
} from "react-icons/fa6";
import Input from "@/components/Input";
import { Api } from "@/services/api";
import type { CreateAttachmentData } from "@/services/attachments";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Toasts } from "@/services/toats";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
}

export default function CreateAttachmentFormSection({
  project,
  cardList,
  card,
}: Props) {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<CreateAttachmentData>({
    card: card.id,
    filename: "",
  });

  const router = useRouter();
  const queryClient = useQueryClient();
  const createMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .attachments()
        .create(data)
        .then(() => {
          Toasts.success("Anexo criado com sucesso!");

          queryClient.invalidateQueries({
            queryKey: [
              "projects",
              project.id,
              "cards-lists",
              cardList.id,
              "cards",
            ],
          });

          router.push(
            `/home/teams/${project.team}/projects/${project.id}/card-lists/${cardList.id}/cards/${card.id}/attachments`,
          );
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

  const onChangeFile = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.currentTarget.files && e.currentTarget.files.length > 0) {
      const file = e.currentTarget.files.item(0) as File;

      setData((data) => ({
        ...data,
        file,
        filename: file.name,
      }));
    }
  };

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
        Criar novo anexo
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
            name="filename"
            type="text"
            placeholder="Nome do arquivo"
            label="Nome do arquivo"
            disabled={isPending}
            icon={FaPencil}
            value={data.filename}
            onChange={onChange}
            errors={errors}
            error={error}
            hiddenError={!!error}
          />
          <Input
            name="file"
            type="file"
            placeholder="Nenhum arquivo selecionado"
            filename={data.file?.name}
            onChange={onChangeFile}
            icon={FaFilePdf}
            disabled={isPending}
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
