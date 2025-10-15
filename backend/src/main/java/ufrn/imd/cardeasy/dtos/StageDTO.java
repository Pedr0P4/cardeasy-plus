package ufrn.imd.cardeasy.dtos;

import java.sql.Date;

public record StageDTO(
  String name,
  Boolean current,
  String description,
  Date expectedStartIn,
  Date expectedEndIn
) {}
