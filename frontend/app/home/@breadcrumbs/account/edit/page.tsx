import Link from "next/link";
import { FaUserPen, FaHouse } from "react-icons/fa6";

export default function ProjectBreadcrumbs() {
  return (
    <>
      <li>
        <Link href="/home">
          <FaHouse />
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
