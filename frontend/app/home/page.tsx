import clsx from "clsx";
import Teams from "@/components/teams/Teams";

export default async function HomePage() {
  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-6 p-6",
      )}
    >
      <section className="w-full flex flex-col gap-2">
        <Teams />
      </section>
    </main>
  );
}
