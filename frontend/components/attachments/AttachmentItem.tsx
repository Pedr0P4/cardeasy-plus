"use client";

import clsx from "clsx";
import type { Attachment } from "@/services/attachments";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import AttachmentContextMenu from "./AttachmentContextMenu";
import { filesize } from "filesize";
import { FaPaperclip } from "react-icons/fa6";

interface Props {
  attachment: Attachment;
  card: Card;
  project: Project;
  cardList: CardList;
  disabled?: boolean;
  onClick: () => void;
}

export default function AttachmentItem({
  attachment,
  card,
  cardList,
  project,
  onClick,
  disabled = false,
}: Props) {
  return (
    <li className="relative w-full" tabIndex={-1}>
      <button
        type="button"
        disabled={disabled}
        onClick={onClick}
        className={clsx(
          "btn bg-base-300 h-18 flex flex-row",
          "items-center justify-start",
          "rounded-md px-5 py-4 gap-4 relative w-full",
        )}
      >
        <div className="flex flex-col justify-start items-start flex-1 overflow-hidden">
          <div className="flex flex-row gap-1.5 items-center justify-start w-full">
            <FaPaperclip/>
            <h3 className="text-lg font-semibold truncate w-full pr-8 text-start">
              {attachment.filename}
            </h3>
          </div>
          <p className="font-light -mt-1 text-start truncate w-full pr-8 text-start">
            {filesize(attachment.size)}
          </p>
        </div>
      </button>
      <AttachmentContextMenu
        attachment={attachment}
        card={card}
        cardList={cardList}
        project={project}
      />
    </li>
  );
}
