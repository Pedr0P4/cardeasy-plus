package ufrn.imd.cardeasy.dtos.account;

import java.util.UUID;

public record AccountDTO(
  UUID id,
  String name,
  String email
) {};
