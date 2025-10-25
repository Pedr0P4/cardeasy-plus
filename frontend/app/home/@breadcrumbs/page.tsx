import { FaHouse } from "react-icons/fa6";

export default async function HomeBreadcrumbs() {
  return (
    <li>
      <span className="inline-flex items-center gap-2">
        <FaHouse />
        Início
      </span>
    </li>
  );
}
