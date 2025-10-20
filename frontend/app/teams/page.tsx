"use client";

import { useAccount } from "@/stores/useAccount";

export default function TeamsPage() {
  const account = useAccount((state) => state.account);

  return <h1>{JSON.stringify(account, null, 2)}</h1>;
}
