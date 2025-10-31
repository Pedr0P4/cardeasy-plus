import { Api } from "@/services/api";
import { Participation, Role } from "@/services/teams";
import {
  FaArrowDown,
  FaArrowRightArrowLeft,
  FaArrowUp,
  FaDoorOpen,
  FaGavel,
  FaGear,
} from "react-icons/fa6";
import { useState } from "react";
import { ApiErrorResponse } from "@/services/base/axios";
import { useRouter } from "next/navigation";

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

  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<Record<string,string>>();
  const router = useRouter();

  // TODO - Completar métodos

  const onPromoteToAdmin = async () => {
    setError("");
    setErrors(undefined);

    if(participation.role == Role.ADMIN){
      setError("Participante já é administrador!");
      return;
    }

    await Api.client()
      .participations().update({
        accountId: participation.account.id,
        teamId: participation.team.id,
        role: Role.ADMIN
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
    });

    router.refresh();
  };

  const onDemoteToMember = async () => {
    setError("");
    setErrors(undefined);

    if(participation.role == Role.MEMBER){
      setError("Participante já é membro!");
      return;
    }

    await Api.client()
      .participations().update({
        accountId: participation.account.id,
        teamId: participation.team.id,
        role: Role.MEMBER
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
      });

    router.refresh();
  };

  const onTransferOwnership = async () => {
    setError("");
    setErrors(undefined);

    if(viewer.role != Role.OWNER){
      setError("Usuário não possui posse!");
      return;
    }

    await Api.client()
      .participations()
      .update({
        accountId: participation.account.id,
        teamId: participation.team.id,
        role: viewer.role
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
      });

    await Api.client()
      .participations()
      .update({
        accountId: viewer.account.id,
        teamId: viewer.team.id,
        role: participation.role
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
      });

    router.refresh();
  };

  const onKick = async () => {
    setError("");
    setErrors(undefined);

    if(viewer.role != Role.ADMIN && viewer.role != Role.OWNER){
      setError("Você não possui permissão para expulsar" + participation.account.name);
      return;
    }

    await Api.client()
      .participations().delete({
        accountId: participation.account.id,
        teamId: participation.team.id
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
      });

    router.refresh();
  };

  // Embora eu tenha colocado separado aqui,
  // na prática o onExit é um caso específico do
  // on kick
  const onExit = async () => {
    setError("");
    setErrors(undefined);

    await Api.client()
      .participations().delete({
        accountId: viewer.account.id,
        teamId: viewer.team.id
      }).catch((err: ApiErrorResponse) => {
        if(err.isValidationError()) setErrors(err.errors);
        else if(err.isErrorResponse()) setError(err.error);
        else setError("Erro inesperado!");
      });

    router.push('/home');
  };

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
