"use client";

import clsx from "clsx";
import Link from "next/link";
import { redirect } from "next/navigation";
import { FaBars } from "react-icons/fa6";
import { FaPenToSquare, FaRightToBracket } from "react-icons/fa6";
import { Api } from "@/services/api";
import { useAccount } from "@/stores/useAccount";
import Avatar from "./Avatar";

export default function Header() {
  const account = useAccount((state) => state.account);

  const onLogout = () => {
    Api.client()
      .accounts()
      .logout()
      .then(() => {
        redirect("/login");
      });
  };

  return (
    <header className="navbar bg-base-300 shadow-sm px-6 py-4 z-10">
      <div className="flex-1">
        <p className="text-2xl font-semibold">
          Cardeasy<span className="text-neutral">+</span>
        </p>
      </div>
      {account && (
        <div className="flex flex-row gap-4 items-center">
          <div className="flex flex-col items-end not-md:hidden">
            <h2 className="font-semibold">{account.name}</h2>
            <p className="font-thin text-sm -mt-1.5">{account.email}</p>
          </div>
          <div className="relative">
            <div className="not-md:hidden">
              <Avatar
                className="!size-8"
                name={account.name ?? ""}
                avatar={account.avatar}
              />
            </div>
            <details
              className={clsx(
                "md:absolute dropdown dropdown-end",
                "bottom-0 right-0 md:translate-x-1/2 md:translate-y-2",
              )}
            >
              <summary
                className={clsx("btn btn-md md:btn-xs btn-neutral btn-square")}
              >
                <FaBars />
              </summary>
              <ul
                tabIndex={-1}
                className={clsx(
                  "menu dropdown-content bg-base-300",
                  "rounded-box z-50 mt-1.5 w-52 p-2 shadow-sm",
                )}
              >
                <li>
                  <Link href="/home/account/edit">
                    <FaPenToSquare /> Editar
                  </Link>
                </li>
                <li>
                  <button
                    type="button"
                    className="text-primary"
                    onClick={onLogout}
                  >
                    <FaRightToBracket />
                    Sair
                  </button>
                </li>
              </ul>
            </details>
          </div>
        </div>
      )}
    </header>
  );
}
