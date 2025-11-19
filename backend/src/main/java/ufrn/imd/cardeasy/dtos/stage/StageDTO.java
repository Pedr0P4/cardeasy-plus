package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.models.StageState;
import ufrn.imd.cardeasy.models.StageStatus;

public record StageDTO(
  @Schema(description = "ID", example = "0")
  Integer id,

  @Schema(description = "Name", example = "Tests")
  String name,

  @Schema(description = "State", example = "STARTED")
  StageState state,

  @Schema(description = "Status", example = "RUNNING")
  StageStatus status,

  @Schema(description = "Description", example = "Time to test")
  String description,

  @Schema(description = "Expeceted start in", example = "02/10/2002")
  Date expectedStartIn,

  @Schema(description = "Expeceted end in", example = "02/10/2002")
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
