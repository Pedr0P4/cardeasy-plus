"use client";

import { useEffect, useRef } from "react";
import type { Account } from "@/services/accounts";
import { useAccount } from "@/stores/useAccount";
import { Api } from "../services/api";

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
    Api.client()
      .images()
      .urlToData(`/avatars/${account.id}.webp`)
      .then((res) => {
        useAccount.getState().setAvatar(res);
      })
      .catch(() => {});
  }, [account.id]);

  return null;
}
