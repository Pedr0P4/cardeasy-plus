import { useContextSelector } from "use-context-selector";
import { accoditionContext } from "./accoditionContext";

export default function useAccordition() {
  const open = useContextSelector(accoditionContext, (state) => state.open);
  const setOpen = useContextSelector(
    accoditionContext,
    (state) => state.setOpen,
  );

  return { open, setOpen };
}
