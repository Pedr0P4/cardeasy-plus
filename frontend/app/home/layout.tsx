import Header from "@/components/Header";
import QueriesContext from "@/components/queries/QueriesContext";

export default function TeamsLayout({
  children,
  breadcrumbs,
}: Readonly<{
  children: React.ReactNode;
  breadcrumbs: React.ReactNode;
}>) {
  return (
    <QueriesContext>
      <div className="flex flex-col w-full sticky top-0 z-20">
        {" "}
        <Header />
        {breadcrumbs}
      </div>
      {children}
    </QueriesContext>
  );
}
