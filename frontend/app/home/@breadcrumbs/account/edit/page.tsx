import Link from "next/link";
import { FaHome } from "react-icons/fa";
import { FaUserPen } from "react-icons/fa6";

export default function ProjectBreadcrumbs() {
  return (
    <>
      <li>
        <Link href="/home">
          <FaHome />
          Início
        </Link>
      </li>
      <li>
        <span className="inline-flex items-center gap-2">
          <FaUserPen />
          Editar conta
        </span>
      </li>
    </>
  );
}
