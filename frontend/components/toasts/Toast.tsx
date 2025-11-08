"use client";

import clsx from "clsx";
import {
  animate,
  type HTMLMotionProps,
  motion,
  useMotionValue,
} from "motion/react";
import { useEffect } from "react";
import { FaCheckCircle } from "react-icons/fa";
import { FaTriangleExclamation, FaX } from "react-icons/fa6";
import { type Notification, useNotifications } from "@/stores/useNotifications";

interface Props extends HTMLMotionProps<"div"> {
  notification: Notification;
  isCurrent: boolean;
  buttonClassName?: string;
}

export default function Toast({
  notification,
  isCurrent,
  className,
  ...props
}: Props) {
  const remove = useNotifications((state) => state.remove);
  const progress = useMotionValue(1);

  const toastClassName = "alert-error";
  let buttonClassName = "btn-error";
  let Icon = FaTriangleExclamation;

  switch (notification.type) {
    case "SUCCESS":
      className = "alert-success";
      buttonClassName = "btn-success";
      Icon = FaCheckCircle;
      break;
    case "WARNING":
      className = "alert-warning";
      buttonClassName = "btn-warning";
      break;
    default:
      break;
  }

  useEffect(() => {
    if (isCurrent) {
      const controls = animate(progress, 0, {
        duration: notification.remainingTime / 1000,
        ease: "linear",
      });

      if (!notification.timeout) controls.stop();

      return () => controls.stop();
    }
  }, [isCurrent, notification.remainingTime, notification.timeout, progress]);

  return (
    <motion.div
      layout
      role="alert"
      initial={{ opacity: 0.25, x: 50 }}
      animate={{ opacity: 1, y: 0, x: 0 }}
      exit={{ opacity: 0, y: -20 }}
      className={clsx(
        "relative border-none overflow-hidden alert shadow-xl h-min",
        toastClassName,
        className,
      )}
      {...props}
    >
      <Icon className="size-6" />
      <span className="text-md -ml-2 font-bold">{notification.content}</span>
      <button
        type="button"
        onClick={() => remove(notification)}
        className={clsx("btn btn-sm btn-circle btn-soft", buttonClassName)}
      >
        <FaX />
      </button>
      <motion.div
        className="absolute w-full bottom-0 left-0 h-1.5 bg-base-content opacity-75"
        style={{
          scaleX: progress,
          transformOrigin: "left",
        }}
      />
    </motion.div>
  );
}
