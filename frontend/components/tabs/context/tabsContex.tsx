"use client";

import React, { useCallback, useState } from "react";
import { createContext } from "use-context-selector";

interface Props {
  initial: string;
  children: React.ReactNode;
}

interface TabsContext {
  tab: string;
  setTab: (tab: string) => void;
}

export const tabsContext = createContext<TabsContext>({} as TabsContext);

export default function TabsContext({ initial, children }: Props) {
  const [tab, setTab] = useState(initial);

  const _setTab = useCallback((tab: string) => setTab(tab), []);

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
