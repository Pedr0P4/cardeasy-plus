package ufrn.imd.cardeasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticateAccountDTO(
  @NotBlank 
  String email,

  @NotNull 
  String password
) {};
