import { headers } from "next/headers";
import { redirect } from "next/navigation";
import AccountStoreInitializer from "@/components/AccountStoreInitializer";
import type { Account } from "@/services/accounts";
import { imageUrlToData } from "@/services/image";

export default async function TeamsLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const _headers = await headers();
  const _account = _headers.get("x-user-account");

  try {
    const account: Account = JSON.parse(_account ?? "");

    return (
      <>
        <AccountStoreInitializer account={account} />
        {children}
      </>
    );
  } catch (_) {
    redirect("/");
  }
}
