import { useNotifications } from "@/stores/useNotifications";

// biome-ignore lint/complexity/noStaticOnlyClass: not-needed
export class Toasts {
  public static success(text: string) {
    useNotifications.getState().success(text);
  }

  public static warning(text: string) {
    useNotifications.getState().warning(text);
  }

  public static error(text: string) {
    useNotifications.getState().error(text);
  }
}
