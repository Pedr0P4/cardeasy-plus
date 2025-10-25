import Link from "next/link";
import {
  FaFileContract,
  FaHouse,
  FaPlus,
  FaUserGroup,
  FaUsers,
} from "react-icons/fa6";

export default async function ProjectBreadcrumbs() {
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
          <FaPlus />
          Criar novo time
        </span>
      </li>
    </>
  );
}
