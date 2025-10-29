"use client";

import clsx from "clsx";
import useTabs from "./context/useTabs";

interface Props {
  name: string;
  children: React.ReactNode;
}

export default function TabButton({ name, children }: Props) {
  const { tab, setTab } = useTabs();

  const onClick = () => {
    setTab(name);
  };

  return (
    <button
      onClick={onClick}
      type="button"
      role="tab"
      className={clsx("tab", tab === name && "tab-active")}
    >
      {children}
    </button>
  );
}
