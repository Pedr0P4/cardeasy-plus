package ufrn.imd.cardeasy.dtos.account;

import java.util.UUID;

import ufrn.imd.cardeasy.models.Account;

public record AccountDTO(
  UUID id,
  String name,
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
