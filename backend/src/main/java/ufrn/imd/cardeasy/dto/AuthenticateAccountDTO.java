package ufrn.imd.cardeasy.dto;

public record AuthenticateAccountDTO(
  String name,
  String email,
  String password
) {};
