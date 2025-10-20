"use client";

import clsx from "clsx";
import Link from "next/link";
import { redirect } from "next/navigation";
import { type ChangeEvent, type FormEvent, useState } from "react";
import { FaTriangleExclamation } from "react-icons/fa6";
import Avatar from "@/components/Avatar";
import Input from "@/components/Input";
import { type RegisterData, register } from "@/services/accounts";
import type { ApiErrorResponse } from "@/services/axios";

export default function RegisterPage() {
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

    const success = await register(data)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isValidationError()) setErrors(err.errors);
        else if (err.isErrorResponse()) setError(err.error);
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect(`/?email=${data.email}`);
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
        "h-screen w-screen bg-base-100 flex flex-col",
        "items-center justify-center",
      )}
    >
      <h1 className="text-4xl font-semibold">
        Cardeasy<span className="text-neutral">+</span>
      </h1>
      <form
        onSubmit={onSubmit}
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-xs border p-4 m-4",
        )}
      >
        <div className="flex flex-row gap-4 w-full items-center">
          <Avatar
            name={data.name}
            avatar={data.avatar}
            onClearAvatar={onClearAvatar}
            onLoadAvatar={onLoadAvatar}
          />
          <Input
            name="name"
            type="text"
            placeholder="Nome"
            label="Nome"
            value={data.name}
            onChange={onChange}
            errors={errors}
            error={error}
            hiddenError={!!error}
          />
        </div>
        <Input
          name="email"
          type="text"
          placeholder="Email"
          label="Email"
          value={data.email}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <Input
          name="password"
          type="password"
          placeholder="Senha"
          label="Senha"
          value={data.password}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
        />
        <button type="submit" className="btn btn-neutral mt-2">
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
        <div role="alert" className="alert alert-error alert-soft w-xs">
          <FaTriangleExclamation className="size-4 -mr-1" />
          <span className="first-letter:uppercase">{error}</span>
        </div>
      )}
    </main>
  );
}
