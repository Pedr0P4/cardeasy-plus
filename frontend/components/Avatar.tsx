import { ImageData } from "@/services/image";
import ImageInput from "./ImageInput";
import { FaUpload } from "react-icons/fa";
import clsx from "clsx";
import Image from "next/image";

interface Props {
  name: string;
  avatar?: ImageData;
  onLoadAvatar?: (base64: string, blob: Blob, filename?: string) => void;
  onClearAvatar?: () => void;
}

export default function Avatar({
  name,
  avatar,
  onClearAvatar,
  onLoadAvatar,
}: Props) {
  const hasImage = !!avatar?.url;

  return (
    <div className={clsx("relative avatar", !hasImage && "avatar-placeholder")}>
      <div
        className={clsx(
          "ring-neutral bg-base-100 ring-offset-base-200",
          "size-12 rounded-full ring-2 ring-offset-2",
        )}
      >
        {hasImage ? (
          <Image
            src={avatar?.url ?? ""}
            alt="user avatar"
            width={300}
            height={300}
          />
        ) : (
          <span className="text-xl">
            {name.length > 0 ? name[0].toUpperCase() : "U"}
          </span>
        )}
      </div>
      {onClearAvatar && onLoadAvatar && (
        <ImageInput
          className={clsx(
            "absolute -bottom-2 -right-2 btn btn-xs",
            "btn-circle btn-neutral pl-[1px]",
          )}
          imageSize={{
            aspect: 1,
            height: 300,
            width: 300,
          }}
          file={{
            url: avatar?.url ?? "",
            name: avatar?.filename,
          }}
          canClear={!!avatar}
          onImageClear={onClearAvatar}
          onImageLoaded={onLoadAvatar}
        >
          <FaUpload />
        </ImageInput>
      )}
    </div>
  );
}
