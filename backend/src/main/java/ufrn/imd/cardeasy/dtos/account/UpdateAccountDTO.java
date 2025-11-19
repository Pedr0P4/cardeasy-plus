package ufrn.imd.cardeasy.dtos.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAccountDTO(
  @NotBlank
  @Size(min = 3, max = 45)
  @Schema(description = "Name", example = "Lucas Marcel Silva de Brito")
  String name,

  @NotBlank
  @Email
  @Size(min = 3, max = 45)
  @Schema(description = "E-mail", example = "marcel@gmail.com")
  String email,
  
  @NotBlank
  @Schema(description = "Password", example = "marcel", format = "password")
  String password,

  @Size(min = 6, max = 36)
  @Schema(description = "Password", example = "marcel", format = "password")
  String newPassword
) {};
