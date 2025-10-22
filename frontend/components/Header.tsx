"use client";

import { useAccount } from "@/stores/useAccount";
import Avatar from "./Avatar";

export default function Header() {
  const account = useAccount((state) => state.account);

  return (
    <header className="navbar bg-base-300 shadow-sm pl-6 pr-8 py-4">
      <div className="flex-1">
        <p className="text-2xl font-semibold">
          Cardeasy<span className="text-neutral">+</span>
        </p>
      </div>
      <div className="flex-none">
        <Avatar name={account?.name ?? ""} avatar={account?.avatar} />
      </div>
    </header>
  );
}
