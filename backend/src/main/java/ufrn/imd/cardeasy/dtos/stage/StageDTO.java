package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;
import java.util.List;

import ufrn.imd.cardeasy.models.Stage;

public record StageDTO(
  Integer id,
  String name,
  Boolean current,
  String description,
  Date expectedStartIn,
  Date expectedEndIn
) {
  public static StageDTO from(
    Stage stage
  ) {
    return new StageDTO(
      stage.getId(),
      stage.getName(),
      stage.getCurrent(),
      stage.getDescription(),
      stage.getExpectedStartIn(),
      stage.getExpectedEndIn()
    );
  };

  public static List<StageDTO> from(
    List<Stage> stages
  ) {
    return stages.stream()
      .map(StageDTO::from)
      .toList();
  };
};
