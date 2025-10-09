interface Props {
  id: string;
};

export default function Hero({ id }: Props) {
  return (
    <div className="hero bg-base-200 min-h-[60vh]">
      <div className="hero-content text-center">
        <div className="max-w-md">
          <h1 className="text-4xl font-bold">Exemplo de componente</h1>
          <p className="pt-6 pb-2">
            Copiei esse componente quase inteiro do 
            <span className="badge badge-neutral px-1.5 mx-0.5">Daisy UI</span>. 
            As cores estão meio alteradas porque eu não limpei o 
            <span className="badge badge-neutral px-1.5 mx-0.5">CSS</span> do template.
            O time que você está tentando acessar é o: 
          </p>
          <p className="pb-6 text-nowrap">{id}</p>
          <a 
            href="https://daisyui.com/components/"
            target="_blank"
            rel="noopener noreferrer"
            className="btn btn-primary"
          >
            Lista de componentes
          </a>
        </div>
      </div>
    </div>
  );
};
