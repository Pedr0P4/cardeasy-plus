package ufrn.imd.cardeasy.dtos.assignment;

import java.util.UUID;

import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAssignmentDTO(  
  @NonNull
  @Schema(description = "Account ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID account,

  @NonNull
  @Schema(description = "Card ID", example = "1")
  Integer card
) {};
