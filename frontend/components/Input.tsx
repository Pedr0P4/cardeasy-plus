"use client";

import clsx from "clsx";
import {
  useId,
  type ChangeEvent,
  type DetailedHTMLProps,
  type InputHTMLAttributes,
} from "react";

interface Props
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  optional?: boolean;
  onChangeOptional?: (event: ChangeEvent<HTMLInputElement>) => void;
  label?: string;
  error?: string;
  errors?: Record<string, string>;
  hiddenError?: boolean;
}

export default function Input({
  label = "",
  error = "",
  errors,
  name = "",
  optional = false,
  className,
  disabled,
  onChangeOptional = () => {},
  hiddenError = false,
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
      {(!optional || !disabled) && (
        <input
          id={inputId}
          className={clsx("input w-full", message && "validator", className)}
          aria-invalid={message ? true : undefined}
          name={name}
          disabled={disabled}
          {...props}
        />
      )}
      {message && !hiddenError && (
        <div className="validator-hint visible first-letter:uppercase mt-0 ">
          {message}
        </div>
      )}
    </div>
  );
}
