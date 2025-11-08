package ufrn.imd.cardeasy.dtos.assignment;

public record AssignmentCandidateDTO(
  String team,
  String account,
  String name,
  String email,
  String role,
  Boolean assigned
) {};
