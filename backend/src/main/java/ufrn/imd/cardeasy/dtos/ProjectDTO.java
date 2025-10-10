package ufrn.imd.cardeasy.dtos;

import ufrn.imd.cardeasy.models.Budget;

public record ProjectDTO(
    int index,
    String title,
    String description,
) {}
