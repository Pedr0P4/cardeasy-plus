"use client";

import clsx from "clsx";
import { useRouter } from "next/navigation";

export default function ErrorPage({
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  const router = useRouter();

  return (
    <main
      className={clsx(
        "h-screen w-full bg-base-100 flex flex-col",
        "items-center justify-center not-sm:bg-base-200",
        "not-sm:justify-start gap-2 p-12",
      )}
    >
      <h2 className="text-lg font-semibold lg:max-w-2/3 text-center">
        Erro inesperado!
      </h2>
      <p className="lg:max-w-1/2 text-center">
        Ocorreu um erro inesperado ao se comunicar com o servidor!
      </p>
      <div className="flex flex-row flex-wrap max-w-full justify-center gap-4 mt-2">
        <button
          className="btn btn-neutral"
          type="button"
          onClick={() => router.back()}
        >
          Voltar para a página anterior
        </button>
        <button className="btn" type="button" onClick={() => reset()}>
          Tentar novamente
        </button>
      </div>
    </main>
  );
}
