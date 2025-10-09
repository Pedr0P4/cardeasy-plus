import clsx from "clsx";
import Image from "next/image";
import Link from "next/link";

export default function HomePage() {
  return (
    <div className={clsx(
      "font-sans grid grid-rows-[20px_1fr_20px]",
      "items-center justify-items-center min-h-screen",
      "p-8 pb-20 gap-16 sm:p-20"
    )}>
      <main className={clsx(
        "flex flex-col gap-[32px] row-start-2",
        "items-center sm:items-start"
      )}>
        <Image
          className="dark:invert"
          src="/next.svg"
          alt="Next.js logo"
          width={180}
          height={38}
          priority
        />
        <ol className={clsx(
          "font-mono list-inside list-decimal",
          "text-sm/6 text-center sm:text-left"
        )}>
          <li className="tracking-[-.01em]">
            Editei essa página para guiar vocês.
          </li>
          <li className="tracking-[-.01em]">
            Experimenta editar{" "}
            <code className={clsx(
              "bg-black/[.05] dark:bg-white/[.06]",
              "font-mono font-semibold px-1 py-0.5 rounded"
            )}>
              app/page.tsx
            </code>
            .
          </li>
          <li className="tracking-[-.01em]">
            Salve e veja as alterações serem renderizadas.
          </li>
          <li className="tracking-[-.01em]">
            Essa página não está usando o <code className={clsx(
              "bg-black/[.05] dark:bg-white/[.06]",
              "font-mono font-semibold px-1 py-0.5 rounded"
            )}>
              Daisy UI
            </code> ainda.
          </li>
          <li className="tracking-[-.01em]">
            Eu apenas aproveitei o template inicial.
          </li>
          <li className="tracking-[-.01em]">
            Clique em <code className={clsx(
              "bg-black/[.05] dark:bg-white/[.06]",
              "font-mono font-semibold px-1 py-0.5 rounded"
            )}>
              próximo
            </code> para ver o exemplo que deixei para vocês.
          </li>
        </ol>
        <div className="flex gap-4 items-center flex-col sm:flex-row">
          <Link
            className={clsx(
              "rounded-full border border-solid border-transparent",
              "transition-colors flex items-center justify-center",
              "bg-foreground text-background gap-2",
              "hover:bg-[#383838] dark:hover:bg-[#ccc]", 
              "font-medium text-sm sm:text-base h-10 sm:h-12", 
              "px-4 sm:px-5 sm:w-auto"
            )}
            href="/teams/ca85079e-978f-4b95-8e73-04b09282fce6"
          >
            <Image
              className="dark:invert"
              src="/vercel.svg"
              alt="Vercel logomark"
              width={20}
              height={20}
            />
            Próximo
          </Link>
          <a
            className={clsx(
              "rounded-full border border-solid border-black/[.08]dark:border-white/[.145]", 
              "transition-colors flex items-center justify-center", 
              "hover:bg-[#f2f2f2] dark:hover:bg-[#1a1a1a]", 
              "hover:border-transparent font-medium text-sm sm:text-base",
              "h-10 sm:h-12 px-4 sm:px-5 w-full sm:w-auto md:w-[236px]"
            )}
            href="https://nextjs.org/docs?utm_source=create-next-app&utm_medium=appdir-template-tw&utm_campaign=create-next-app"
            target="_blank"
            rel="noopener noreferrer"
          >
            Documentação do Next.js
          </a>
        </div>
      </main>
    </div>
  );
};
