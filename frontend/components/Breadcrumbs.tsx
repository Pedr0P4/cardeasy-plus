import type { UUID } from "crypto";
import { headers } from "next/headers";
import Link from "next/link";
import type { IconType } from "react-icons";
import { FaHome, FaProjectDiagram, FaQuestion } from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { getProject, type Project } from "@/services/projects";
import { getTeam, type Team } from "@/services/teams";

type Breadcrumb = {
  title: string;
  path: string;
  navigable: boolean;
  Icon: IconType;
};

type BreadcrumbMatcher = {
  getTitle: (id: string) => string | Promise<string>;
  match: RegExp;
  Icon: IconType;
};

const matchers: BreadcrumbMatcher[] = [
  {
    getTitle: () => "Times",
    Icon: FaHome,
    match: /^\/teams$/g,
  },
  {
    getTitle: async (id) => {
      const team = await getProject(Number.parseInt(id)).catch(
        () =>
          ({
            id: Number.parseInt(id),
            title: "Desconhecido",
            description: "...",
          }) as Project,
      );

      return team.title;
    },
    Icon: FaProjectDiagram,
    match: /\/teams\/([^/]*)\/([^/]*)$/g,
  },
  {
    getTitle: async (id) => {
      const team = await getTeam(id as UUID).catch(
        () =>
          ({
            id,
            title: "Desconhecido",
            description: "...",
          }) as Team,
      );

      return team.title;
    },
    Icon: FaUserGroup,
    match: /\/teams\/([^/]*)$/g,
  },
];

async function getTranslatedBreadcrumb(
  last: string,
  pathname: string,
  navigable: boolean,
): Promise<Breadcrumb> {
  for (const matcher of matchers) {
    if (pathname.match(matcher.match))
      return {
        title: await matcher.getTitle(last),
        Icon: matcher.Icon,
        path: pathname,
        navigable,
      };
  }

  return {
    title: "Desconhecido",
    Icon: FaQuestion,
    path: pathname,
    navigable,
  };
}

async function getBreadcrumbs(
  pathname: string,
  navigable = false,
): Promise<Breadcrumb[]> {
  const path = pathname.split("/").filter((breadcrumb) => breadcrumb.trim());

  if (path.length <= 0) return [];

  const last = path[path.length - 1];
  const breadcrumbs: Breadcrumb[] = [
    await getTranslatedBreadcrumb(last, pathname, navigable),
  ];

  const others = await getBreadcrumbs(`/${path.slice(0, -1).join("/")}`, true);
  breadcrumbs.push(...others);

  return breadcrumbs;
}

export default async function Breadcrumbs() {
  //const _headers = await headers();
  //const pathname = _headers.get("x-pathname") ?? "";
  const breadcrumbs = await getBreadcrumbs(
    "/teams/6adb4645-88f8-4a85-a2ea-e6edc6f20002/1",
  );

  if (breadcrumbs.length <= 0) return null;
  console.log(breadcrumbs);

  return (
    <div className="breadcrumbs text-sm px-6 bg-base-200">
      <ul>
        {breadcrumbs.reverse().map(({ Icon, navigable, path, title }) => {
          return (
            <li key={path}>
              {navigable ? (
                <Link href={path}>
                  {<Icon />}
                  {title}
                </Link>
              ) : (
                <span className="inline-flex items-center gap-2">
                  {<Icon />}
                  {title}
                </span>
              )}
            </li>
          );
        })}
      </ul>
    </div>
  );
}
