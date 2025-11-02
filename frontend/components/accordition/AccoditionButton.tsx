"use client";

import type {
  ButtonHTMLAttributes,
  DetailedHTMLProps,
  ReactElement,
} from "react";
import useAccordition from "./context/useAccordition";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  showIcon: ReactElement;
  hiddenIcon: ReactElement;
}

export default function AccoditionButton({
  children,
  showIcon,
  hiddenIcon,
  ...props
}: Props) {
  const { open, setOpen } = useAccordition();

  const onClick = () => {
    setOpen(!open);
  };

  return (
    <button onClick={onClick} type="button" {...props}>
      {open ? hiddenIcon : showIcon}
      {children}
    </button>
  );
}
