package ufrn.imd.cardeasy.dtos;

import java.sql.Date;

public record BudgetDTO(
  Double minValue,
  Double maxValue,
  String currency,
  Date deadline
) {}
