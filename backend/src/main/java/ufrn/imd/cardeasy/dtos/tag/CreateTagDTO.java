package ufrn.imd.cardeasy.dtos.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTagDTO(
  @NotNull
  Integer project, 

  @NotNull
  Integer card,

  @NotBlank
  @Size(min = 3, max = 30)
  String content
) {};
