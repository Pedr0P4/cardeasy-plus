import Link from "next/link";
import { FaDungeon, FaHouse } from "react-icons/fa6";

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
          <FaDungeon />
          Entrar por código
        </span>
      </li>
    </>
  );
}
