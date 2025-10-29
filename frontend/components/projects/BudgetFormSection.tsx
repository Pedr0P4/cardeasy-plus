"use client";

import { Api } from "@/services/api";
import { ApiErrorResponse } from "@/services/base/axios";
import { Role } from "@/services/teams";
import clsx from "clsx";
import { redirect } from "next/navigation";
import { ChangeEvent, FormEvent, useState } from "react";
import {
  FaCheck,
  FaClipboardList,
  FaCoins,
  FaDollarSign,
  FaFloppyDisk,
  FaPenClip,
  FaPenRuler,
  FaPiggyBank,
  FaPlus,
  FaTrash,
  FaTriangleExclamation,
  FaX,
} from "react-icons/fa6";
import Input from "../Input";
import { Project, UpdateProjectData } from "@/services/projects";
import { UpdateBudgetData } from "@/services/budgets";

interface Props {
  project: Project;
}

export default function BudgetFormSection({ project }: Props) {
  const [hasBudget, setHasBudget] = useState<boolean>(!!project.budget);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<UpdateBudgetData>({
    currency: project.budget?.currency ?? "BRL",
    minValue: project.budget?.minValue ?? 0,
    maxValue: project.budget?.maxValue ?? 0,
    deadline: project.budget?.deadline,
  });

  // TODO - Colocar o input de data (deadline)
  // TODO - Alterar input de quantia (estou pensando em receber como texto msm
  // e formatar dentro), lembrar do numeric keyboard
  // TODO - Ver questão de formatação dos erros

  const onAddBudget = () => {
    setHasBudget(true);
  };

  const onDeleteBudget = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    if (!project.budget) {
      setHasBudget(false);
      return;
    }

    const success = await Api.client()
      .projects()
      .delete(project.id)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect(`/home/teams/${project.team}/projects/${project.id}`);
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    let success = false;

    if (project?.budget) {
      success = await Api.client()
        .budgets()
        .update(project.budget.id, data)
        .then(() => true)
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          return false;
        });
    } else {
      success = await Api.client()
        .budgets()
        .create({ ...data, project: project.id })
        .then(() => true)
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          return false;
        });
    }

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
        <FaPiggyBank className="size-6" />
        Editar verba
      </h1>
      <div
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full rounded-none border",
          "py-4 px-4 sm:px-6  m-0",
        )}
      >
        {hasBudget ? (
          <form
            onSubmit={onSubmit}
            className={clsx("flex flex-col gap-4", "w-full sm:max-w-lg")}
          >
            <Input
              name="currency"
              type="select"
              placeholder="Moeda"
              label="Moeda"
              icon={FaCoins}
              value={data.currency}
              onChange={onChange}
              errors={errors}
              error={error}
              hiddenError={!!error}
            >
              {Intl.supportedValuesOf("currency").map((value) => {
                const formatter = new Intl.NumberFormat("pt-BR", {
                  style: "currency",
                  currency: value,
                });

                const parts = formatter.formatToParts(0);
                const symbol = parts.find(
                  (part) => part.type === "currency",
                )?.value;

                return (
                  <option key={value} value={value}>
                    {value} / {symbol}
                  </option>
                );
              })}
            </Input>
            <Input
              name="minValue"
              type="number"
              placeholder="Verba mínima"
              label="Verba mínima"
              icon={FaCoins}
              value={data.minValue}
              onChange={onChange}
              errors={errors}
              error={error}
              hiddenError={!!error}
            />
            <Input
              name="maxValue"
              type="number"
              placeholder="Verba máxima"
              label="Verba máxima"
              icon={FaCoins}
              value={data.maxValue}
              onChange={onChange}
              errors={errors}
              error={error}
              hiddenError={!!error}
            />
            <div className="flex flex-row flex-wrap gap-4">
              <button type="submit" className="btn btn-neutral">
                {project.budget ? (
                  <>
                    <FaFloppyDisk />
                    Salvar alterações
                  </>
                ) : (
                  <>
                    <FaCheck />
                    Confirmar
                  </>
                )}
              </button>
              <button
                onClick={onDeleteBudget}
                type="button"
                className="btn btn-soft btn-primary"
              >
                {project.budget ? (
                  <>
                    <FaTrash />
                    Remover verba
                  </>
                ) : (
                  <>
                    <FaX />
                    Cancelar
                  </>
                )}
              </button>
            </div>
          </form>
        ) : (
          <div className={clsx("flex flex-col gap-2", "w-full sm:max-w-lg")}>
            <p className="font-thin">
              Esse projeto não possue nenhuma verba alocada.
            </p>
            <div className="flex flex-row flex-wrap gap-4">
              <button
                type="button"
                onClick={onAddBudget}
                className="btn btn-neutral"
              >
                <FaPlus />
                Adicionar verba
              </button>
            </div>
          </div>
        )}
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
