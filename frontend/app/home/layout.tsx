import { headers } from "next/headers";
import { redirect } from "next/navigation";
import AccountStoreInitializer from "@/components/AccountStoreInitializer";
import Header from "@/components/Header";
import type { Account } from "@/services/accounts";

export default async function TeamsLayout({
  children,
  breadcrumbs,
}: Readonly<{
  children: React.ReactNode;
  breadcrumbs: React.ReactNode;
}>) {
  const _headers = await headers();
  const _account = _headers.get("x-user-account");

  try {
    const account: Account = JSON.parse(_account ?? "");

    return (
      <>
        <AccountStoreInitializer account={account} />
        <div className="flex flex-col w-full sticky top-0">
          {" "}
          <Header />
          {breadcrumbs}
        </div>
        {children}
      </>
    );
  } catch (_) {
    redirect("/");
  }
}
