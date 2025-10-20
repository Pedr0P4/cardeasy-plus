import { create } from "zustand";
import type { Account } from "@/services/accounts";
import type { ImageData } from "@/services/image";

export interface AccountStore {
  account?: Account;
  setAccount: (account: Account) => void;
  setAvatar: (avatar: ImageData) => void;
}

export const useAccount = create<AccountStore>((set, get) => ({
  setAccount: (account) => set({ account }),
  setAvatar: (avatar: ImageData) => {
    if (get().account)
      set({
        account: {
          ...(get().account as Account),
          avatar,
        },
      });
  },
}));
