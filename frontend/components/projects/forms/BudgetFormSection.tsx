"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import {
  type ChangeEvent,
  type Dispatch,
  type SetStateAction,
  useMemo,
  useState,
} from "react";
import {
  FaCalendarDay,
  FaCheck,
  FaCoins,
  FaFloppyDisk,
  FaPiggyBank,
  FaPlus,
  FaTrash,
  FaTriangleExclamation,
  FaX,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import type { Budget, UpdateBudgetData } from "@/services/budgets";
import type { Project } from "@/services/projects";
import Input from "../../Input";

interface Props {
  project: Project;
  isLoading: boolean;
  setIsLoading: Dispatch<SetStateAction<boolean>>;
}

export default function BudgetFormSection({
  project,
  isLoading,
  setIsLoading,
}: Props) {
  const [hasBudget, setHasBudget] = useState<boolean>(!!project.budget);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [withDeadline, setWithDeadline] = useState(!!project.budget?.deadline);
  const [data, setData] = useState<UpdateBudgetData>({
    currency: project.budget?.currency ?? "BRL",
    minValue: project.budget?.minValue ?? 0,
    maxValue: project.budget?.maxValue ?? 0,
    deadline: project.budget?.deadline,
  });

  const queryClient = useQueryClient();
  const deleteMutation = useMutation({
    mutationFn: async (budget: Budget) => {
      return await Api.client()
        .budgets()
        .delete(budget.id)
        .then(() => {
          queryClient.invalidateQueries({ queryKey: ["projects", project.id] });
          setHasBudget(false);
          setIsLoading(false);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const updateMutation = useMutation({
    mutationFn: async (budget: Budget) => {
      return await Api.client()
        .budgets()
        .update(budget.id, {
          ...data,
          deadline: withDeadline ? data.deadline : undefined,
        })
        .then(() => {
          queryClient.invalidateQueries({ queryKey: ["projects", project.id] });
          setIsLoading(false);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const createMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .budgets()
        .create({
          ...data,
          deadline: withDeadline ? data.deadline : undefined,
          project: project.id,
        })
        .then(() => {
          queryClient.invalidateQueries({ queryKey: ["projects", project.id] });
          setIsLoading(false);
        })
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          throw err;
        });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const formatter = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: data.currency,
    minimumFractionDigits: 2,
  });

  const formattedMinValue = useMemo(
    () => formatter.format(data.minValue),
    [data.minValue, formatter],
  );

  const formattedMaxValue = useMemo(
    () => formatter.format(data.maxValue),
    [data.maxValue, formatter],
  );

  const onAddBudget = () => {
    setHasBudget(true);
  };

  const onChangeWithDeadline = (e: ChangeEvent<HTMLInputElement>) => {
    setWithDeadline(e.target.checked);
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const onChangeDeadline = (date?: Date) =>
    setData((data) => ({
      ...data,
      deadline: date ? date.getTime() : undefined,
    }));

  const onChangeCurrency = (e: ChangeEvent<HTMLInputElement>) => {
    const digits = e.target.value.replace(/\D/g, "");
    if (!digits) {
      setData((data) => ({
        ...data,
        [e.target.name]: 0,
      }));
    } else {
      setData((data) => ({
        ...data,
        [e.target.name]: parseFloat(digits) / 100,
      }));
    }
  };

  const isPending =
    isLoading ||
    updateMutation.isPending ||
    deleteMutation.isPending ||
    createMutation.isPending;

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
            onSubmit={(e) => {
              e.preventDefault();
              setError("");
              setErrors({});
              setIsLoading(true);
              if (project?.budget) {
                updateMutation.mutate(project.budget);
              } else {
                createMutation.mutate();
              }
            }}
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
              disabled={isPending}
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
              type="text"
              inputMode="numeric"
              placeholder="Verba mínima"
              label="Verba mínima"
              icon={FaCoins}
              value={formattedMinValue}
              onChange={onChangeCurrency}
              errors={errors}
              error={error}
              hiddenError={!!error}
              disabled={isPending}
            />
            <Input
              name="maxValue"
              type="text"
              inputMode="numeric"
              placeholder="Verba máxima"
              label="Verba máxima"
              icon={FaCoins}
              value={formattedMaxValue}
              onChange={onChangeCurrency}
              errors={errors}
              error={error}
              hiddenError={!!error}
              disabled={isPending}
            />
            <Input
              name="deadline"
              type="day"
              optional
              disabled={isPending}
              hidden={!withDeadline}
              onChangeOptional={onChangeWithDeadline}
              placeholder="Prazo de conclusão"
              label="Com prazo de conclusão"
              icon={FaCalendarDay}
              selected={data.deadline ? new Date(data.deadline) : undefined}
              onSelect={onChangeDeadline}
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
                disabled={isPending}
                onClick={() => {
                  setError("");
                  setErrors({});

                  if (!project.budget) {
                    setHasBudget(false);
                    return;
                  }

                  setIsLoading(true);
                  deleteMutation.mutate(project.budget);
                }}
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
                disabled={isPending}
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
