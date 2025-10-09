import Hero from "@/components/Hero";
import clsx from "clsx";

export default async function TeamPage({
  params,
}: {
  params: Promise<{ id: string }>
}) {
  const { id } = await params;

  //Aqui a gente pegaria o tima do backend
  //const team = await getTeam(id);

  return (
    <div className={clsx(
      "items-center justify-items-center min-h-screen"
    )}>
      <Hero id={id}/>
      <section className="flex flex-col px-4 py-6 w-full gap-4">
        <div className="collapse collapse-arrow bg-base-100 border border-base-300">
          <input type="radio" name="my-accordion-2" defaultChecked />
          <div className="collapse-title font-semibold">Como assim ID do time?</div>
          <div className="collapse-content text-sm">
            É só para você ver como funciona os parâmetros da rota, experimenta mudar lá na URL.
          </div>
        </div>
        <div className="collapse collapse-arrow bg-base-100 border border-base-300">
          <input type="radio" name="my-accordion-2" />
          <div className="collapse-title font-semibold">Onde encontro o arquivo desta página?</div>
          <div className="collapse-content text-sm">
            Em <span className="badge badge-neutral px-1.5 mx-0.5">app/teams/[id]/page.tsx</span>.
            Boa parte eu também só peguei do <span className="badge badge-neutral px-1.5 mx-0.5">Daisy UI</span>.
          </div>
        </div>
      </section>
    </div>
  );
};
