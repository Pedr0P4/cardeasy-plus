package ufrn.imd.cardeasy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountDTO(
  @NotNull
  @Size(min = 3, max = 45)
  String name,
  
  @NotBlank
  @Email
  @Size(min = 0, max = 45)
  String email,

  @NotNull
  @Size(min = 6, max = 36)
  String password
) {};
