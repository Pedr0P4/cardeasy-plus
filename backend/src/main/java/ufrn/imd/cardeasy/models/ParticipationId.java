package ufrn.imd.cardeasy.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ParticipationId implements Serializable {
  private UUID accountId;
  private UUID teamId;
};
