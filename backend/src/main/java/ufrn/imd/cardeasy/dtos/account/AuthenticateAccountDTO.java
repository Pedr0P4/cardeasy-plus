package ufrn.imd.cardeasy.dtos.account;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateAccountDTO(
  @NotBlank 
  String email,

  @NotBlank
  String password
) {};
