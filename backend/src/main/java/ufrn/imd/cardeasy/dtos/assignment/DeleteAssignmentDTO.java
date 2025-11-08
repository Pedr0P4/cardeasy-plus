package ufrn.imd.cardeasy.dtos.assignment;

import java.util.UUID;

import io.micrometer.common.lang.NonNull;

public record DeleteAssignmentDTO(  
  @NonNull
  UUID account,

  @NonNull
  Integer card
) {};
