package ufrn.imd.cardeasy.dto;

import java.util.UUID;

public record AccountDTO(
  UUID id,
  String name,
  String email
) {};
