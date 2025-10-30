import clsx from "clsx";
import { DayPicker, type DayPickerProps } from "react-day-picker";
import { ptBR } from "react-day-picker/locale";
import type { IconType } from "react-icons";

export type DateInputProps = Omit<DayPickerProps, "mode"> & {
  placeholder?: string;
  selected?: Date;
  message?: string;
  icon?: IconType;
};

export default function DateInput({
  selected,
  className,
  placeholder,
  message,
  icon: Icon,
  ...props
}: DateInputProps) {
  console.log(selected);
  return (
    <>
      <button
        type="button"
        popoverTarget="rdp-popover"
        style={{ anchorName: "--rdp" } as React.CSSProperties}
        className={clsx("input w-full", message && "validator", className)}
        aria-invalid={message ? true : undefined}
      >
        {Icon && <Icon className={clsx(message && "text-error")} />}
        {selected ? selected.toLocaleDateString() : placeholder}
      </button>
      <div
        popover="auto"
        id="rdp-popover"
        className="dropdown"
        style={{ positionAnchor: "--rdp" } as React.CSSProperties}
      >
        <DayPicker
          locale={ptBR}
          aria-invalid={message ? true : undefined}
          className={clsx("react-day-picker", className)}
          mode="single"
          selected={selected}
          {...props}
        />
      </div>
    </>
  );
}
