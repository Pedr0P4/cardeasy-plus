package ufrn.imd.cardeasy.dtos.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticateAccountDTO(
  @NotBlank
  @Schema(description = "E-mail", example = "marcel@gmail.com")
  String email,
  
  @NotBlank
  @Schema(description = "Password", example = "marcel", format = "password")
  String password
) {};
