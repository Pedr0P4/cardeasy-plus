"use client";

import clsx from "clsx";
import Link from "next/link";
import { redirect, useSearchParams } from "next/navigation";
import {
  type ChangeEvent,
  type FormEvent,
  useState,
  useTransition,
} from "react";
import {
  FaEnvelope,
  FaKey,
  FaTriangleExclamation,
  FaUnlock,
} from "react-icons/fa6";
import Input from "@/components/Input";
import type { LoginData } from "@/services/accounts";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import { Toasts } from "@/services/toats";

export default function LoginPage() {
  const params = useSearchParams();
  const [isLoading, startTransition] = useTransition();
  const [error, setError] = useState<string>("");
  const [data, setData] = useState<LoginData>({
    email: params.get("email") ?? "",
    password: "",
  });

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");

    startTransition(async () => {
      const success = await Api.client()
        .accounts()
        .login(data)
        .then(() => true)
        .catch((err: ApiErrorResponse) => {
          if (err.isApiError()) setError("usuário ou senha incorretos");
          else setError("erro inesperado");
          return false;
        });

      if (success) {
        Toasts.success("Login realizado com sucesso!");
        redirect("/home");
      }
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
        <Input
          name="email"
          type="text"
          placeholder="Email"
          label="Email"
          icon={FaEnvelope}
          value={data.email}
          onChange={onChange}
          error={error}
          hiddenError
          disabled={isLoading}
        />
        <Input
          name="password"
          type="password"
          placeholder="Senha"
          label="Senha"
          icon={FaKey}
          value={data.password}
          onChange={onChange}
          error={error}
          hiddenError
          disabled={isLoading}
        />

        <button disabled={isLoading} type="submit" className="btn btn-neutral">
          <FaUnlock />
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
