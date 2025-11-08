import type { UUID } from "node:crypto";
import { create } from "zustand";

export type NotificationType = "SUCCESS" | "WARNING" | "ERROR";

export type Notification = {
  uuid: UUID;
  type: NotificationType;
  content: string;
  timeout?: NodeJS.Timeout;
  duration: number;
  startTime: number;
  remainingTime: number;
};

export interface NotificationsStore {
  clear: () => void;
  removeById: (uuid: UUID) => void;
  remove: (notification: Notification) => void;
  resume: (notification: Notification) => void;
  resumeNext: () => void;
  pause: (notification: Notification) => void;
  notify: (text: string, type: NotificationType, duration?: number) => void;
  pauseNext: () => void;
  success: (text: string) => void;
  warning: (text: string) => void;
  error: (text: string) => void;
  notifications: Notification[];
}

export const useNotifications = create<NotificationsStore>((set, get) => ({
  notifications: [],
  clear: () => {
    get().notifications.forEach((notification) => {
      clearTimeout(notification.timeout);
    });

    set({ notifications: [] });
  },
  removeById: (uuid) => {
    const notification = get().notifications.find(
      (notification) => notification.uuid === uuid,
    );
    if (notification) get().remove(notification);
  },
  remove: (notification) => {
    clearTimeout(notification.timeout);

    const now = Date.now();
    const notifications = [
      ...get()
        .notifications.filter(
          (_notification) => _notification.uuid !== notification.uuid,
        )
        .map((notification, i) => {
          if (i === 0) {
            const newTimeout = setTimeout(() => {
              get().remove(notification);
            }, notification.remainingTime);

            clearTimeout(notification.timeout);

            return {
              ...notification,
              timeout: newTimeout,
              startTime: now,
            };
          }

          return notification;
        }),
    ];

    set({ notifications });
  },
  resume: (notification) => {
    clearTimeout(notification.timeout);

    const now = Date.now();
    const newTimeout = setTimeout(() => {
      get().remove(notification);
    }, notification.remainingTime);

    const notifications = [
      ...get().notifications.map((_notification) => {
        if (_notification.uuid === notification.uuid) {
          return {
            ..._notification,
            timeout: newTimeout,
            startTime: now,
          };
        }

        return _notification;
      }),
    ];

    set({ notifications });
  },
  resumeNext: () => {
    if (get().notifications.length > 0) {
      const next = get().notifications[0];
      get().resume(next);
    }
  },
  pause: (notification) => {
    if (notification.timeout) {
      clearTimeout(notification.timeout);
      const timeElapsed = Date.now() - notification.startTime;
      const remainingTime = notification.remainingTime - timeElapsed;

      const notifications = [
        ...get().notifications.map((_notification) => {
          if (_notification.uuid === notification.uuid) {
            return {
              ..._notification,
              remainingTime,
              timeout: undefined,
            };
          }

          return _notification;
        }),
      ];

      set({ notifications });
    }
  },
  pauseNext: () => {
    if (get().notifications.length > 0) {
      const next = get().notifications[0];
      get().pause(next);
    }
  },
  notify: (text, type, duration = 4000) => {
    const now = Date.now();

    const uuid = crypto.randomUUID() as UUID;
    const notifications = [
      {
        uuid,
        type,
        content: text,
        duration,
        startTime: Date.now(),
        remainingTime: duration,
        timeout: setTimeout(() => {
          get().removeById(uuid);
        }, duration),
      },
      ...get().notifications.map((notification) => {
        if (notification.timeout) {
          clearTimeout(notification.timeout);

          const timeElapsed = now - notification.startTime;
          const remainingTime = notification.remainingTime - timeElapsed;

          return {
            ...notification,
            remainingTime,
            timeout: undefined,
          };
        }
        return notification;
      }),
    ];

    set({ notifications });
  },
  success: (text) => get().notify(text, "SUCCESS"),
  warning: (text) => get().notify(text, "WARNING", 12000),
  error: (text) => get().notify(text, "ERROR", 5000),
}));
