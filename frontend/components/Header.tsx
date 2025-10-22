"use client";

import { useAccount } from "@/stores/useAccount";
import Avatar from "./Avatar";
import { FaBars, FaPencilAlt, FaPencilRuler } from "react-icons/fa";
import clsx from "clsx";
import {
  FaDoorOpen,
  FaPencil,
  FaPenToSquare,
  FaRightToBracket,
} from "react-icons/fa6";

export default function Header() {
  const account = useAccount((state) => state.account);

  return (
    <header className="navbar bg-base-300 shadow-sm pl-6 pr-8 py-4">
      <div className="flex-1">
        <p className="text-2xl font-semibold">
          Cardeasy<span className="text-neutral">+</span>
        </p>
      </div>
      {account && (
        <div className="flex flex-row gap-4 items-center">
          <div className="flex flex-col items-end">
            <h2 className="font-semibold">{account.name}</h2>
            <p className="font-thin text-sm -mt-1.5">{account.email}</p>
          </div>
          <div className="relative">
            <Avatar
              className="!size-8"
              name={account.name ?? ""}
              avatar={account.avatar}
            />
            <div
              className={clsx(
                "absolute dropdown dropdown-end",
                "bottom-0 right-0 translate-x-1/2 translate-y-2",
              )}
            >
              <button
                tabIndex={0}
                type="button"
                className={clsx("btn btn-xs btn-neutral btn-square", "")}
              >
                <FaBars />
              </button>
              <ul
                tabIndex={-1}
                className="menu dropdown-content bg-base-300 rounded-box z-1 mt-4 w-52 p-2 shadow-sm"
              >
                <li>
                  <button type="button">
                    <FaPenToSquare /> Editar
                  </button>
                </li>
                <li>
                  <button type="button" className="text-primary">
                    <FaRightToBracket />
                    Sair
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      )}
    </header>
  );
}
