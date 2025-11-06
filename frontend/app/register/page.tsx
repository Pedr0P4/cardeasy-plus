"use client";

import clsx from "clsx";
import Link from "next/link";
import { redirect } from "next/navigation";
import {
  type ChangeEvent,
  type FormEvent,
  useState,
  useTransition,
} from "react";
import {
  FaEnvelope,
  FaKey,
  FaPencil,
  FaTriangleExclamation,
  FaUser,
} from "react-icons/fa6";
import Avatar from "@/components/Avatar";
import Input from "@/components/Input";
import type { RegisterData } from "@/services/accounts";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";

export default function RegisterPage() {
  const [isLoading, startTransition] = useTransition();
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [data, setData] = useState<RegisterData>({
    name: "",
    email: "",
    password: "",
  });

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    startTransition(async () => {
      const success = await Api.client()
        .accounts()
        .register(data)
        .then(() => true)
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          return false;
        });

      if (success) redirect(`/login?email=${data.email}`);
    });
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const onClearAvatar = () =>
    setData((data) => ({
      ...data,
      avatar: undefined,
    }));

  const onLoadAvatar = (base64: string, blob: Blob, filename?: string) => {
    const url = URL.createObjectURL(blob);
    setData((data) => ({
      ...data,
      avatar: {
        url,
        blob,
        base64,
        filename,
      },
    }));
  };

  return (
    <main
      className={clsx(
        "h-screen w-full bg-base-100 flex flex-col",
        "items-center justify-center not-sm:bg-base-200",
        "not-sm:justify-start",
      )}
    >
      <h1
        className={clsx(
          "text-4xl font-semibold not-sm:py-6",
          "not-sm:text-2xl not-sm:self-start",
          "not-sm:px-4 bg-base-100 not-sm:w-full",
        )}
      >
        Cardeasy<span className="text-neutral">+</span>
      </h1>
      <form
        onSubmit={onSubmit}
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full not-sm:rounded-none sm:w-xs border",
          "p-4 m-4 not-sm:m-0",
        )}
      >
        <div className="flex flex-row gap-4 w-full items-center">
          <Avatar
            disabled={isLoading}
            name={data.name}
            avatar={data.avatar}
            onClearAvatar={onClearAvatar}
            onLoadAvatar={onLoadAvatar}
          />
          <Input
            disabled={isLoading}
            name="name"
            type="text"
            placeholder="Nome"
            label="Nome"
            icon={FaUser}
            value={data.name}
            onChange={onChange}
            errors={errors}
            error={error}
            hiddenError={!!error}
          />
        </div>
        <Input
          disabled={isLoading}
          name="email"
          type="text"
          placeholder="Email"
          label="Email"
          icon={FaEnvelope}
          value={data.email}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <Input
          disabled={isLoading}
          name="password"
          type="password"
          placeholder="Senha"
          label="Senha"
          icon={FaKey}
          value={data.password}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <button disabled={isLoading} type="submit" className="btn btn-neutral">
          <FaPencil />
          Registrar-se
        </button>
        <p className="text-sm">
          Já tem uma conta?{" "}
          <Link
            className="underline underline-offset-2 hover:opacity-75"
            href="/"
          >
            entre
          </Link>
        </p>
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
