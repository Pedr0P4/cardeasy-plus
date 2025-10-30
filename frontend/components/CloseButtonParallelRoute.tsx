"use client";

import { useRouter } from "next/navigation";
import { FaX } from "react-icons/fa6";

export default function CloseButtonParallelRoute() {
  const router = useRouter();

  return (
    <button
      type="button"
      onClick={() => router.back()}
      className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
    >
      <FaX />
    </button>
  );
};
