"use client";

import clsx from "clsx";
import {
  type ChangeEvent,
  type DetailedHTMLProps,
  type InputHTMLAttributes,
  SelectHTMLAttributes,
  type TextareaHTMLAttributes,
  useId,
} from "react";
import type { IconType } from "react-icons";

interface InputProps
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {}

interface TextareaProps
  extends DetailedHTMLProps<
    TextareaHTMLAttributes<HTMLTextAreaElement>,
    HTMLTextAreaElement
  > {}

interface SelectProps
  extends DetailedHTMLProps<
    SelectHTMLAttributes<HTMLSelectElement>,
    HTMLSelectElement
  > {}

type Props = {
  optional?: boolean;
  onChangeOptional?: (event: ChangeEvent<HTMLInputElement>) => void;
  label?: string;
  error?: string;
  errors?: Record<string, string>;
  hiddenError?: boolean;
  icon?: IconType;
} & (
  | ({
      type: "textarea";
    } & TextareaProps)
  | ({
      type: "select";
    } & SelectProps)
  | InputProps
);

export default function Input({
  label = "",
  error = "",
  errors,
  name = "",
  icon: Icon,
  optional = false,
  className,
  disabled,
  onChangeOptional = () => {},
  hiddenError = false,
  type,
  ...props
}: Props) {
  const inputId = useId();
  const checkboxId = useId();

  const message = error ? error : errors ? errors[name] : error;

  return (
    <div className="flex flex-col gap-1.5 w-full">
      {label && (
        <label
          htmlFor={optional ? checkboxId : inputId}
          className="label text-sm"
        >
          {optional && (
            <input
              id={checkboxId}
              type="checkbox"
              onChange={onChangeOptional}
              checked={!disabled}
              className="mb-0.5 checkbox checkbox-neutral checkbox-sm rounded-md"
            />
          )}
          <span className={clsx(!optional && "pl-0.5")}>{label}</span>
        </label>
      )}
      {(!optional || !disabled) &&
        (type === "textarea" ? (
          <label className="relative">
            {Icon && (
              <Icon
                className={clsx("absolute m-3 z-10", message && "text-error")}
              />
            )}
            <textarea
              id={inputId}
              aria-invalid={message ? true : undefined}
              name={name}
              disabled={disabled}
              className={clsx(
                "textarea w-full",
                message && "validator",
                "pl-8.5 !scrollbar !scrollbar-thin",
                "scrollbar-thumb-base-content",
                "scrollbar-track-base-200",
                className,
              )}
              {...(props as TextareaProps)}
            />
          </label>
        ) : type === "select" ? (
          <label
            className={clsx("select w-full", message && "validator", className)}
            aria-invalid={message ? true : undefined}
          >
            {Icon && <Icon className={clsx(message && "text-error")} />}
            <select
              id={inputId}
              aria-invalid={message ? true : undefined}
              name={name}
              disabled={disabled}
              className={clsx("!ml-0 !pl-0.5", className)}
              {...(props as SelectProps)}
            />{" "}
          </label>
        ) : (
          <label
            className={clsx("input w-full", message && "validator", className)}
            aria-invalid={message ? true : undefined}
          >
            {Icon && <Icon className={clsx(message && "text-error")} />}
            <input
              id={inputId}
              aria-invalid={message ? true : undefined}
              name={name}
              disabled={disabled}
              type={type}
              {...(props as InputProps)}
            />{" "}
          </label>
        ))}
      {message && !hiddenError && (
        <div className="validator-hint visible first-letter:uppercase mt-0 ">
          {message}
        </div>
      )}
    </div>
  );
}
