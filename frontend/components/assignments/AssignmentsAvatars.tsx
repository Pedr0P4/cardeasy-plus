import type { Assignment } from "@/services/assignments";
import AssingmentsAvatar from "./AssignmentAvatar";

interface Props {
  card: number;
  assignments: Assignment[];
  total: number;
}

export default function AssingmentsAvatars({
  card,
  assignments,
  total,
}: Props) {
  return (
    <div className="avatar-group -space-x-3">
      {assignments.map((assignment) => (
        <AssingmentsAvatar
          assignment={assignment}
          key={`cards-${card}-assignment-${assignment.account}-avatar-${assignment.team}`}
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
