import type { Participation } from "@/services/participations";
import AssingmentsAvatar from "./AssignmentAvatar";

interface Props {
  participations: Participation[];
  total: number;
}

export default function AssingmentsAvatars({ participations, total }: Props) {
  return (
    <div className="avatar-group -space-x-3">
      {participations.map((participation) => (
        <AssingmentsAvatar
          participation={participation}
          key={`assignment-avatar-${participation.account.id}`}
        />
      ))}
      {total > 2 && (
        <div className="avatar avatar-placeholder border-none">
          <div className="bg-base-200 text-neutral-content size-6">
            <span className="text-sm">+{total - 2}</span>
          </div>
        </div>
      )}
    </div>
  );
}
