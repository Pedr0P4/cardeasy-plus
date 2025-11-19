package ufrn.imd.cardeasy.dtos.account;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Account;

public record AccountDTO(
  @Schema(description = "ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID id,

  @Schema(description = "Name", example = "Lucas Marcel")
  String name,

  @Schema(description = "Name", example = "marcel@gmail.com")
  String email
) {
  public static AccountDTO from(Account account) {
    return new AccountDTO(
      account.getId(),
      account.getName(),
      account.getEmail()
    );
  };
};
