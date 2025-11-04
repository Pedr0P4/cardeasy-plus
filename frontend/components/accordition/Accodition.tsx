"use client";

import useAccordition from "./context/useAccordition";

interface Props {
  children: React.ReactNode;
}

export default function Accodition({ children }: Props) {
  const { open } = useAccordition();

  if (open) return children;
  return null;
}
