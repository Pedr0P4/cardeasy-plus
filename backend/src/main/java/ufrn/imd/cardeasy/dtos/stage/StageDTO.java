package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;

public record StageDTO(
  Integer id,
  String name,
  Boolean current,
  String description,
  Date expectedStartIn,
  Date expectedEndIn
) {};
