package ar.edu.utn.frc.tup.lciii.dtos;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCountryRequest {
    @Max(10)
    private Integer amountOfCountryToSave;
}
