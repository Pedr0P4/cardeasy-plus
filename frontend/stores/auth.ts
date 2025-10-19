import { create } from "zustand";
import { persist } from "zustand/middleware";

export type AccountStore = {
  token: string;
  setToken: (token: string) => void;
};

export const useAuth = create<AccountStore>()(
  persist(
    (set) => ({
      token: "",
      setToken: (token) => set({ token }),
    }),
    {
      name: "cardeasy@auth",
      partialize: (state) => ({ token: state.token }),
    },
  ),
);
