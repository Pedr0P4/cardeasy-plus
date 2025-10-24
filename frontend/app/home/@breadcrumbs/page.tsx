import { FaHome } from "react-icons/fa";

export default async function HomeBreadcrumbs() {
  return (
    <li>
      <span className="inline-flex items-center gap-2">
        <FaHome />
        Início
      </span>
    </li>
  );
}
