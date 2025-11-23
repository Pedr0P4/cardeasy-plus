"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import { useRouter } from "next/navigation";
import { type ChangeEvent, useEffect, useState } from "react";
import {
  FaFilePdf,
  FaFloppyDisk,
  FaPencil,
  FaPenRuler,
  FaTrash,
  FaTriangleExclamation,
} from "react-icons/fa6";
import Input from "@/components/Input";
import { Api } from "@/services/api";
import type { Attachment, CreateAttachmentData } from "@/services/attachments";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Toasts } from "@/services/toats";

interface Props {
  project: Project;
  cardList: CardList;
  card: Card;
  attachment: Attachment;
}

export default function EditAttachmentFormSection({
  project,
  cardList,
  card,
  attachment
}: Props) {
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<CreateAttachmentData>({
    card: card.id,
    filename: attachment.filename
  });

  const router = useRouter();
  const queryClient = useQueryClient();
  const updateMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .attachments()
        .update(attachment.id, data)
        .then(() => {
          Toasts.success("Anexo atualizado com sucesso!");

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

  useEffect(() => {
    setIsLoading(true);
    Api.client()
      .attachments()
      .download(attachment.id)
      .then((file) => {
        setData({
          card: card.id,
          filename: attachment.filename,
          file,
        });
      }).finally(() => setIsLoading(false));
  }, [attachment]);

  const deleteMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .attachments()
        .delete(attachment.id)
        .then(() => {
          Toasts.success("Anexo apagado com sucesso!");

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
        .catch((error: ApiErrorResponse) => {
          if (error.isErrorResponse()) Toasts.error(error.error);
          else Toasts.error("Erro inesperado!");
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

  const isPending =
    isLoading || updateMutation.isPending || deleteMutation.isPending;

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
        Editar anexo
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
            updateMutation.mutate();
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
              <FaFloppyDisk />
              Salvar alterações
            </button>
            <button
              disabled={isPending}
              onClick={() => {
                setError("");
                setErrors({});
                setIsLoading(true);
                deleteMutation.mutate();
              }}
              type="button"
              className="btn btn-soft btn-primary"
            >
              <FaTrash />
              Remover anexo
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
