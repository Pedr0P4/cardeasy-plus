"use client";

import { AnimatePresence } from "motion/react";
import { useNotifications } from "@/stores/useNotifications";
import Toast from "./Toast";

export default function Toasts() {
  const notifications = useNotifications((state) => state.notifications);
  const pauseNext = useNotifications((state) => state.pauseNext);
  const resumeNext = useNotifications((state) => state.resumeNext);

  if (notifications.length <= 0) return null;

  return (
    // biome-ignore lint/a11y/noStaticElementInteractions: not-needed
    <div
      onMouseEnter={pauseNext}
      onMouseLeave={resumeNext}
      className="z-[1000] toast toast-end bottom-4 right-6 lg:right-8 stack h-fit w-md not-lg:w-xs not-md:!w-[80%]"
    >
      <AnimatePresence>
        {notifications.map((notification, index) => (
          <Toast
            key={notification.uuid}
            isCurrent={index === 0}
            notification={notification}
            style={{
              zIndex: notifications.length - index,
            }}
          />
        ))}
      </AnimatePresence>
    </div>
  );
}
