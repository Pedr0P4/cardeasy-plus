package ufrn.imd.cardeasy.dtos.cardlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCardListDTO(
  @NotBlank
  @Size(min = 1, max = 45)
  String title
){
}
