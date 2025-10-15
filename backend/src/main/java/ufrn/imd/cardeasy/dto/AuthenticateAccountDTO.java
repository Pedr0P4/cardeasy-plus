package ufrn.imd.cardeasy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticateAccountDTO(
  @NotBlank 
  String email,

  @NotNull 
  String password
) {};
