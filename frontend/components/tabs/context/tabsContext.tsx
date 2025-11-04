"use client";

import type React from "react";
import {
  type Dispatch,
  type SetStateAction,
  useCallback,
  useState,
} from "react";
import { createContext } from "use-context-selector";

interface Props {
  initial: string;
  children: React.ReactNode;
}

interface TabsContext {
  tab: string;
  setTab: Dispatch<SetStateAction<string>>;
}

export const tabsContext = createContext<TabsContext>({} as TabsContext);

export default function TabsContext({ initial, children }: Props) {
  const [tab, setTab] = useState(initial);

  const _setTab: Dispatch<SetStateAction<string>> = useCallback(
    (tab) => setTab(tab),
    [],
  );

  return (
    <tabsContext.Provider
      value={{
        tab,
        setTab: _setTab,
      }}
    >
      {children}
    </tabsContext.Provider>
  );
}
