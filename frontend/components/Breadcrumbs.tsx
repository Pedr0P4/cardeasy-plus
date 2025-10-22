import type { UUID } from "crypto";
import Link from "next/link";
import type { IconType } from "react-icons";
import {
  FaHome,
  FaProjectDiagram,
  FaQuestion,
  FaWalking,
} from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { Api } from "@/services/api";
import clsx from "clsx";

type Breadcrumb = {
  title: string;
  path: string;
  navigable: boolean;
  Icon: IconType;
};

type BreadcrumbMatcher = {
  getTitle: (id: string) => Promise<string>;
  match: RegExp;
  Icon: IconType;
};

const matchers: BreadcrumbMatcher[] = [
  {
    getTitle: async () => "Início",
    Icon: FaHome,
    match: /^\/teams$/g,
  },
  {
    getTitle: async (id) => {
      const team = await Api.server().projects().get(Number.parseInt(id));
      return team.title;
    },
    Icon: FaProjectDiagram,
    match: /\/teams\/([^/]*)\/([^/]*)$/g,
  },
  {
    getTitle: async (id) => {
      const team = await Api.server()
        .teams()
        .get(id as UUID);

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
    if (pathname.match(matcher.match)) {
      const title = await matcher.getTitle(last).catch(() => "Desconhecido");

      return {
        title,
        Icon: matcher.Icon,
        path: pathname,
        navigable,
      };
    }
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

  return (
    <div
      className={clsx(
        "breadcrumbs text-sm px-6 bg-base-200",
        "flex flex-row justify-between items-center",
      )}
    >
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
