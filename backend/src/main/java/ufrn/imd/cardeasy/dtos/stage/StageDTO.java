package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;
import java.util.List;

import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.models.StageState;
import ufrn.imd.cardeasy.models.StageStatus;

public record StageDTO(
  Integer id,
  String name,
  StageState state,
  StageStatus status,
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
      stage.getState(),
      stage.getStatus(),
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
