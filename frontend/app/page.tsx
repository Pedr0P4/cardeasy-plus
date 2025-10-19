"use client";

import clsx from "clsx";
import { type ChangeEvent, type FormEvent, useState } from "react";
import { type LoginData, login } from "@/services/authentication";
import type { ApiErrorResponse } from "@/services/axios";
import Link from "next/link";

export default function HomePage() {
  const [error, setError] = useState<string>("");

  const [data, setData] = useState<LoginData>({
    email: "",
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
          "fieldset bg-base-200 border-base-300",
          "rounded-box w-xs border p-4 m-4",
        )}
      >
        <label className="label">Email</label>
        <input
          name="email"
          type="text"
          aria-invalid={!!error}
          className="input validator"
          placeholder="Email"
          value={data.email}
          onChange={onChange}
        />
        <label className="label">Senha</label>
        <input
          name="password"
          type="password"
          aria-invalid={!!error}
          className="input validator"
          placeholder="Senha"
          value={data.password}
          onChange={onChange}
        />
        {error && (
          <div className="validator-hint visible first-letter:uppercase">
            {error}
          </div>
        )}
        <button type="submit" className="btn btn-neutral mt-4">
          Entrar
        </button>
        <p>
          Sem uma conta?{" "}
          <Link
            className="underline underline-offset-2 hover:opacity-75"
            href="/register"
          >
            registre-se
          </Link>
        </p>
      </form>
    </main>
  );
}
