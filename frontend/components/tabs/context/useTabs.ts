import { useContextSelector } from "use-context-selector";
import { tabsContext } from "./tabsContex";

export default function useTabs() {
  const tab = useContextSelector(tabsContext, (state) => state.tab);
  const setTab = useContextSelector(tabsContext, (state) => state.setTab);

  return { tab, setTab };
}
