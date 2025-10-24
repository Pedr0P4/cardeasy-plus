"use client";

import clsx from "clsx";
import Link from "next/link";
import { redirect, useSearchParams } from "next/navigation";
import { type ChangeEvent, type FormEvent, useState } from "react";
import { FaTriangleExclamation } from "react-icons/fa6";
import Input from "@/components/Input";
import type { LoginData } from "@/services/accounts";
import type { ApiErrorResponse } from "@/services/base/axios";
import { Api } from "../../services/api";

export default function LoginPage() {
  const params = useSearchParams();
  const [error, setError] = useState<string>("");
  const [data, setData] = useState<LoginData>({
    email: params.get("email") ?? "",
    password: "",
  });

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");

    const success = await Api.client()
      .accounts()
      .login(data)
      .then(() => true)
      .catch((err: ApiErrorResponse) => {
        if (err.isApiError()) setError("usuário ou senha incorretos");
        else setError("erro inesperado");
        return false;
      });

    if (success) redirect("/home");
  };

  const onChange = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  return (
    <main
      className={clsx(
        "h-screen w-full bg-base-100 flex flex-col",
        "items-center justify-center",
      )}
    >
      <h1
        className={clsx(
          "text-4xl font-semibold not-sm:my-6",
          "not-sm:text-2xl not-sm:self-start",
          "not-sm:mx-4",
        )}
      >
        Cardeasy<span className="text-neutral">+</span>
      </h1>
      <form
        onSubmit={onSubmit}
        className={clsx(
          "flex flex-col gap-4 bg-base-200 border-base-300",
          "rounded-box w-full not-sm:rounded-none sm:w-xs border",
          "p-4 m-4 not-sm:flex-1 not-sm:m-0",
        )}
      >
        <Input
          name="email"
          type="text"
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
