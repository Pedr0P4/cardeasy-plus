import clsx from "clsx";
import Link from "next/link";
import { FaDungeon, FaPlus } from "react-icons/fa6";
import Teams from "@/components/teams/Teams";

export default async function HomePage() {
  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-6 p-6",
      )}
    >
      <section className="w-full flex flex-row flex-wrap gap-2">
        <Link href="/home/teams/create" className="btn btn-neutral">
          <FaPlus />
          Criar novo time
        </Link>
        <Link href="/home/teams/join" className="btn btn">
          <FaDungeon />
          Entrar por código
        </Link>
      </section>
      <section className="w-full flex flex-col gap-2">
        <Teams />
      </section>
    </main>
  );
}
