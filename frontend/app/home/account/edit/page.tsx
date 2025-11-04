"use client";

import clsx from "clsx";
import { redirect } from "next/navigation";
import {
  type ChangeEvent,
  type FormEvent,
  useState,
  useTransition,
} from "react";
import {
  FaEnvelope,
  FaFloppyDisk,
  FaKey,
  FaTriangleExclamation,
  FaUser,
  FaUserPen,
} from "react-icons/fa6";
import Avatar from "@/components/Avatar";
import Input from "@/components/Input";
import type { EditAccountData } from "@/services/accounts";
import { Api } from "@/services/api";
import type { ApiErrorResponse } from "@/services/base/axios";
import { useAccount } from "@/stores/useAccount";

export default function EditAccountPage() {
  const account = useAccount((state) => state.account);

  const [isLoading, startTransition] = useTransition();
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string, string>>();
  const [updatePassword, setUpdatePassword] = useState<boolean>(false);
  const [data, setData] = useState<EditAccountData>({
    avatar: account?.avatar,
    name: account?.name ?? "",
    email: account?.email ?? "",
    password: "",
    newPassword: undefined,
  });

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors({});

    if (!account) {
      setError("erro inesperado");
      return;
    }

    startTransition(async () => {
      const success = await Api.client()
        .accounts()
        .update(account.id, data)
        .then(() => true)
        .catch((err: ApiErrorResponse) => {
          if (err.isValidationError()) setErrors(err.errors);
          else if (err.isErrorResponse()) setError(err.error);
          else setError("erro inesperado");
          return false;
        });

      if (success) redirect("/home");
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

  const onChangePassword = (e: ChangeEvent<HTMLInputElement>) =>
    setUpdatePassword(e.target.checked);

  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-center justify-center sm:flex-1 not-sm:bg-base-200",
        "not-sm:justify-start",
      )}
    >
      <h1
        className={clsx(
          "text-2xl font-semibold not-sm:py-4",
          "not-sm:text-xl not-sm:self-start",
          "not-sm:px-4 bg-base-100 not-sm:w-full",
          "flex flex-row items-center gap-2",
        )}
      >
        <FaUserPen className="size-6" />
        Editar conta
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
            name={data.name}
            avatar={data.avatar}
            onClearAvatar={onClearAvatar}
            onLoadAvatar={onLoadAvatar}
            disabled={isLoading}
          />
          <Input
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
            disabled={isLoading}
          />
        </div>
        <Input
          name="email"
          type="text"
          placeholder="Email"
          label="Email"
          value={data.email}
          icon={FaEnvelope}
          onChange={onChange}
          errors={errors}
          error={error}
          hiddenError={!!error}
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
          errors={errors}
          error={error}
          hiddenError={!!error}
          disabled={isLoading}
        />
        <Input
          name="newPassword"
          type="password"
          placeholder="Nova senha"
          label="Alterar senha"
          icon={FaKey}
          value={data.newPassword}
          onChange={onChange}
          errors={errors}
          error={error}
          optional
          hidden={!updatePassword}
          hiddenError={!!error}
          onChangeOptional={onChangePassword}
          disabled={isLoading}
        />
        <button disabled={isLoading} type="submit" className="btn btn-neutral">
          <FaFloppyDisk />
          Salvar
        </button>
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
