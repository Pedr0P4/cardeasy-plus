"use client";

import clsx from "clsx";
import { useState } from "react";
import { FaCheck, FaPen, FaTrash, FaX } from "react-icons/fa6";
import type { Tag } from "@/services/tags";
import Input from "../Input";

interface Props {
  tag: Tag;
  disabled?: boolean;
  onClick: () => void;
  onDelete: () => void;
  onUpdate: (content: string) => void;
}

export default function TagCandidateItem({
  tag,
  onClick,
  onDelete,
  onUpdate,
  disabled = false,
}: Props) {
  const [editing, setEditing] = useState(false);
  const [content, setContent] = useState("");

  return (
    <li className="w-full" tabIndex={-1}>
      {editing ? (
        <div
          className={clsx(
            "btn bg-base-300 h-10 flex flex-row",
            "items-center justify-start",
            "rounded-md px-2 py-4 gap-4 relative w-full",
            tag.used && "btn-neutral border-2",
          )}
        >
          <div className="flex flex-row gap-1.5 items-center justify-start w-full">
            <Input
              id={`tag-content-${tag.id}`}
              disabled={disabled}
              name="content"
              type="text"
              className="input-sm !max-h-[1.625rem]"
              placeholder={tag.content}
              icon={FaPen}
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
            <button
              type="button"
              onClick={(e) => {
                e.stopPropagation();
                setContent("");
                setEditing(false);
              }}
              className="btn btn-xs btn-square btn-primary"
            >
              <FaX />
            </button>
            <button
              type="button"
              disabled={content.length < 3}
              onClick={(e) => {
                e.stopPropagation();
                onUpdate(content);
                setContent("");
                setEditing(false);
              }}
              className="btn btn-xs btn-square btn-secondary"
            >
              <FaCheck />
            </button>
          </div>
        </div>
      ) : (
        <button
          type="button"
          disabled={disabled}
          onClick={onClick}
          className={clsx(
            "btn bg-base-300 h-10 flex flex-row",
            "items-center justify-start",
            "rounded-md px-2 py-4 gap-4 relative w-full",
            tag.used && "btn-neutral border-2",
          )}
        >
          <div className="flex flex-row gap-1.5 items-center justify-start w-full">
            <span className="badge badge-sm">{tag.usages}</span>
            <h3 className="text-md font-semibold truncate w-full pr-8 text-start">
              {tag.content}
            </h3>
            <button
              type="button"
              onClick={(e) => {
                e.stopPropagation();
                setContent(tag.content);
                setEditing(true);
              }}
              className="btn btn-xs btn-square btn-neutral"
            >
              <FaPen />
            </button>
            <button
              type="button"
              onClick={(e) => {
                e.stopPropagation();
                onDelete();
              }}
              className="btn btn-xs btn-square btn-primary"
            >
              <FaTrash />
            </button>
          </div>
        </button>
      )}
    </li>
  );
}
