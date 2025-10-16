package ufrn.imd.cardeasy.dtos.project;

import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;

public record ProjectDTO(
  Integer id,
  String title,
  String description,
  TeamDTO team,
  BudgetDTO budget
) {};
