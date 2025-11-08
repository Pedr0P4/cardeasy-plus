package ufrn.imd.cardeasy.dtos.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTagDTO(
  @NotBlank
  @Size(min = 3, max = 30)
  String content
) {};
