import clsx from "clsx";
import type { DetailedHTMLProps, InputHTMLAttributes } from "react";

interface Props
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
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
  hiddenError = false,
  ...props
}: Props) {
  const message = error ? error : errors ? errors[name] : error;

  return (
    <div className="flex flex-col gap-1.5 w-full">
      {label && <label className="label text-sm pl-0.5">{label}</label>}
      <input
        className={clsx("input", message && "validator")}
        aria-invalid={message ? true : undefined}
        name={name}
        {...props}
      />
      {message && !hiddenError && (
        <div className="validator-hint visible first-letter:uppercase mt-0 ">
          {message}
        </div>
      )}
    </div>
  );
}
