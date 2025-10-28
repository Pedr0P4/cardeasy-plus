"use client";

import useTabs from "./context/useTabs";

interface Props {
  name: string;
  children: React.ReactNode;
}

export default function Tab({ name, children }: Props) {
  const { tab } = useTabs();

  if (tab === name) return children;
  return null;
}
