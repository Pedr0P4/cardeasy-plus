"use client";

import { useEffect, useRef } from "react";
import type { Account } from "@/services/accounts";
import { imageUrlToData } from "@/services/image";
import { useAccount } from "@/stores/useAccount";

interface Props {
  account: Account;
}

export default function AccountStoreInitializer({ account }: Props) {
  const initialized = useRef(false);

  if (!initialized.current) {
    useAccount.getState().setAccount(account);
    initialized.current = true;
  }

  useEffect(() => {
    imageUrlToData(`/avatars/${account.id}.webp`).then((res) => {
      useAccount.getState().setAvatar(res);
    });
  }, [account.id]);

  return null;
}
