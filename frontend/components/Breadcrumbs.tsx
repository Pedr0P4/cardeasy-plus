"use client";

import clsx from "clsx";
import type { UUID } from "crypto";
import Link from "next/link";
import type { IconType } from "react-icons";
import {
  FaHome,
  FaProjectDiagram,
  FaQuestion,
  FaUserEdit,
} from "react-icons/fa";
import { FaUserGroup } from "react-icons/fa6";
import { Api } from "@/services/api";
import { usePathname } from "next/navigation";
import { useEffect, useState, useTransition } from "react";

type Breadcrumb = {
  title: string;
  path: string;
  navigable: boolean;
  consume: number;
  Icon: IconType;
};

type BreadcrumbMatcher = {
  getTitle: (id: string) => Promise<string>;
  match: RegExp;
  consume: number;
  Icon: IconType;
};

const matchers: BreadcrumbMatcher[] = [
  {
    getTitle: async () => "Início",
    Icon: FaHome,
    match: /^\/home$/g,
    consume: 1,
  },
  {
    getTitle: async () => "Editar conta",
    Icon: FaUserEdit,
    match: /^\/home\/account\/edit$/g,
    consume: 2,
  },
  {
    getTitle: async (id) => {
      const team = await Api.client().projects().get(Number.parseInt(id));
      return team.title;
    },
    Icon: FaProjectDiagram,
    match: /^\/home\/teams\/([^/]*)\/projects\/([^/]*)$/g,
    consume: 2,
  },
  {
    getTitle: async (id) => {
      const team = await Api.client()
        .teams()
        .get(id as UUID);

      return team.title;
    },
    Icon: FaUserGroup,
    match: /^\/home\/teams\/([^/]*)$/g,
    consume: 2,
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
        consume: matcher.consume,
        navigable,
      };
    }
  }

  return {
    title: "Desconhecido",
    Icon: FaQuestion,
    consume: 1,
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
  const breadcrumb: Breadcrumb = await getTranslatedBreadcrumb(
    last,
    pathname,
    navigable,
  );
  const breadcrumbs: Breadcrumb[] = [breadcrumb];

  const others = await getBreadcrumbs(
    `/${path.slice(0, -breadcrumb.consume).join("/")}`,
    true,
  );
  breadcrumbs.push(...others);

  return breadcrumbs;
}

export default function Breadcrumbs() {
  const pathname = usePathname();
  const [breadcrumbs, setBreadcrumbs] = useState<Breadcrumb[]>([]);
  const [isPending, startTransition] = useTransition();

  useEffect(() => {
    startTransition(async () => {
      await getBreadcrumbs(pathname).then((breadcrumbs) =>
        setBreadcrumbs(breadcrumbs.reverse()),
      );
    });
  }, [pathname]);

  if (breadcrumbs.length <= 0) return null;

  return (
    <div
      className={clsx(
        "breadcrumbs text-sm px-6 bg-base-200",
        "flex flex-row justify-between items-center",
        "",
      )}
    >
      <ul>
        {isPending ? (
          <>
            <li></li>
          </>
        ) : (
          breadcrumbs.map(({ Icon, navigable, path, title }) => {
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
          })
        )}
      </ul>
    </div>
  );
}
