package ufrn.imd.cardeasy.dtos.account;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountDTO(
  @NotNull
  @org.hibernate.validator.constraints.UUID
  UUID id,

  @NotBlank
  @Size(min = 3, max = 45)
  String name,

  @NotBlank
  @Email
  @Size(min = 0, max = 45)
  String email
) {};
