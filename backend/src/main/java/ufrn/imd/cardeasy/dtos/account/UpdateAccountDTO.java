package ufrn.imd.cardeasy.dtos.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAccountDTO(
  @NotBlank
  @Size(min = 3, max = 45)
  String name,

  @NotBlank
  @Email
  @Size(min = 3, max = 45)
  String email,
  
  @NotBlank
  String password,

  @Size(min = 6, max = 36)
  String newPassword
) {};
