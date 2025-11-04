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
  initial: boolean;
  children: React.ReactNode;
}

interface AccordionContext {
  open: boolean;
  setOpen: Dispatch<SetStateAction<boolean>>;
}

export const accoditionContext = createContext<AccordionContext>(
  {} as AccordionContext,
);

export default function AccordionContext({ initial, children }: Props) {
  const [open, setOpen] = useState(initial);

  const _setOpen: Dispatch<SetStateAction<boolean>> = useCallback(
    (open) => setOpen(open),
    [],
  );

  return (
    <accoditionContext.Provider
      value={{
        open,
        setOpen: _setOpen,
      }}
    >
      {children}
    </accoditionContext.Provider>
  );
}
