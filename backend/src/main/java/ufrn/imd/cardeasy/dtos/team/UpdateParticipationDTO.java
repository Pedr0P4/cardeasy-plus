package ufrn.imd.cardeasy.dtos.team;

import java.util.UUID;

import ufrn.imd.cardeasy.models.Role;

public record UpdateParticipationDTO(UUID accountId, UUID teamId, Role role) {
}
