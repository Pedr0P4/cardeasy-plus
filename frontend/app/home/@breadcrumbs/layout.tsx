import clsx from "clsx";

export default async function Breadcrumbs({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div
      className={clsx(
        "breadcrumbs text-sm px-6 bg-base-200",
        "flex flex-row justify-between items-center",
      )}
    >
      <ul>{children}</ul>
    </div>
  );
}
