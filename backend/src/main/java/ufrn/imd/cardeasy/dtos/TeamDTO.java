package ufrn.imd.cardeasy.dtos;

import jakarta.validation.constraints.Size;

public record TeamDTO(
  String title,
  String description,
  @Size(min = 6, max = 6)
  String invite) {}
