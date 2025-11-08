import clsx from "clsx";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa6";
import Input from "./Input";

interface Props {
  current: number;
  last: number;
  className?: string;
  onChange: (page: number) => void;
}

type PageButton =
  | {
      type: "default";
      page: number;
    }
  | {
      type: "dots";
      index: number;
    };

const getPages = (last: number, current: number): PageButton[] => {
  if (last <= 7) {
    return Array.from({ length: last }, (_, i) => ({
      page: i + 1,
      type: "default",
    }));
  }

  let start: number;
  let end: number;

  if (current <= 4) {
    start = 2;
    end = 5;
  } else if (current >= last - 3) {
    start = last - 4;
    end = last - 1;
  } else {
    start = current - 1;
    end = current + 1;
  }

  const pages: PageButton[] = [
    {
      type: "default",
      page: 1,
    },
  ];

  if (start > 2)
    pages.push({
      type: "dots",
      index: 1,
    });

  for (let i = start; i <= end; i++) {
    pages.push({
      type: "default",
      page: i,
    });
  }

  if (end < last - 1)
    pages.push({
      type: "dots",
      index: 2,
    });

  pages.push({
    type: "default",
    page: last,
  });

  return pages;
};

export default function Pagination({
  last,
  current,
  onChange,
  className,
}: Props) {
  if (last <= 0) return null;

  return (
    <div
      className={clsx(
        "mt-4 mb-2 self-center max-w-full join mt-4 mb-2",
        className,
      )}
    >
      <button
        disabled={current === 0}
        type="button"
        className="join-item btn btn-sm"
        onClick={() => {
          if (current > 0) onChange(current - 1);
        }}
      >
        <FaArrowLeft />
      </button>
      <div className="md:hidden">
        <Input
          name="page"
          type="text"
          inputMode="numeric"
          className="join-item input-sm rounded-none max-w-12"
          disabled={current === last && last === 0}
          value={current + 1}
          onChange={(e) => {
            const digits = e.target.value.replace(/\D/g, "");

            if (!digits) {
              onChange(0);
            } else {
              const page = Number.parseInt(digits, 10) - 1;
              if (!Number.isNaN(page))
                onChange(Math.max(Math.min(page, last), 0));
            }
          }}
        />
      </div>
      <div className="not-md:hidden">
        {getPages(last + 1, current + 1).map((page) => {
          if (page.type === "dots")
            return (
              <button
                key={`page-dots-${page.index}`}
                tabIndex={-1}
                type="button"
                className="join-item btn btn-disabled btn-sm"
              >
                ...
              </button>
            );
          else {
            return (
              <button
                key={`page-${page.page}`}
                onClick={() => onChange(page.page - 1)}
                type="button"
                className={clsx(
                  "join-item btn btn-sm",
                  page.page - 1 === current && "btn-active btn-neutral",
                )}
              >
                {page.page}
              </button>
            );
          }
        })}
      </div>
      <button
        disabled={current === last}
        type="button"
        className="join-item btn btn-sm"
        onClick={() => {
          if (current < last) onChange(current + 1);
        }}
      >
        <FaArrowRight />
      </button>
    </div>
  );
}
