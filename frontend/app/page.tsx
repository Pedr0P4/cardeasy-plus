"use client";

import clsx from "clsx";
import Link from "next/link";
import { type ChangeEvent, type FormEvent, useState } from "react";
import Input from "@/components/Input";
import { type LoginData, login } from "@/services/authentication";
import type { ApiErrorResponse } from "@/services/axios";
import { FaTriangleExclamation } from "react-icons/fa6";
import { useSearchParams } from "next/navigation";

export default function HomePage() {
  const params = useSearchParams();
  const [error, setError] = useState<string>("");
  const [data, setData] = useState<LoginData>({
    email: params.get("email") ?? "",
    password: "",
  });

  const onSubmit = (e: FormEvent) => {
    e.preventDefault();
    setError("");
    login(data).catch((err: ApiErrorResponse) => {
      if (err.isApiError()) setError("usuário ou senha incorretos");
      else setError("erro inesperado");
    });
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

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
        <Input
          name="email"
          type="text"
          className="input validator"
          placeholder="Email"
          label="Email"
          value={data.email}
          onChange={onChange}
          error={error}
          hiddenError
        />
        <Input
          name="password"
          type="password"
          className="input validator"
          placeholder="Senha"
          label="Senha"
          value={data.password}
          onChange={onChange}
          error={error}
          hiddenError
        />

        <button type="submit" className="btn btn-neutral mt-2">
          Entrar
        </button>
        <p className="text-sm">
          Sem uma conta?{" "}
          <Link
            className="underline underline-offset-2 hover:opacity-75"
            href="/register"
          >
            registre-se
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
