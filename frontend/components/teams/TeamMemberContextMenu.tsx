import {
  FaArrowDown,
  FaArrowRightArrowLeft,
  FaArrowUp,
  FaDoorOpen,
  FaGavel,
  FaGear,
} from "react-icons/fa6";
import { type Participation, Role } from "@/services/teams";

interface Props {
  viewer: Participation;
  participation: Participation;
}

const levels = {
  OWNER: 2,
  ADMIN: 1,
  MEMBER: 0,
};

export default function TeamMemberContextMenu({
  viewer,
  participation,
}: Props) {
  const isOnwer = viewer.role === Role.OWNER;
  const same = viewer.account.id === participation.account.id;
  const level = levels[viewer.role] - levels[participation.role];

  // TODO - Completar métodos

  const onPromoteToAdmin = () => {};
  const onDemoteToMember = () => {};
  const onTransferOwnership = () => {};
  const onKick = () => {};

  // Embora eu tenha colocado separado aqui,
  // na prática o onExit é um caso específico do
  // on kick
  const onExit = () => {};

  if (level <= 0 && (!same || (same && isOnwer))) return null;

  return (
    <div className="absolute top-0 right-0 dropdown dropdown-end">
      <button tabIndex={0} type="button" className="btn m-1">
        <FaGear />
      </button>
      <ul
        tabIndex={-1}
        className="dropdown-content menu bg-base-100 rounded-box z-1 w-64 p-2 shadow-sm"
      >
        {isOnwer && !same && (
          <>
            {level >= 2 ? (
              <li>
                <button type="button" onClick={onPromoteToAdmin}>
                  <FaArrowUp />
                  Promover à administrador
                </button>
              </li>
            ) : (
              <li>
                <button type="button" onClick={onDemoteToMember}>
                  <FaArrowDown />
                  Rebaixar à membro
                </button>
              </li>
            )}
            <li>
              <button
                type="button"
                onClick={onTransferOwnership}
                className="text-primary"
              >
                <FaArrowRightArrowLeft />
                Tramsferir pose
              </button>
            </li>
          </>
        )}
        {level >= 1 && !same && (
          <li>
            <button type="button" onClick={onKick} className="text-primary">
              <FaGavel />
              Expulsar do time
            </button>
          </li>
        )}
        {same && !isOnwer && (
          <li>
            <button type="button" onClick={onExit} className="text-primary">
              <FaDoorOpen />
              Sair do time
            </button>
          </li>
        )}
      </ul>
    </div>
  );
}
