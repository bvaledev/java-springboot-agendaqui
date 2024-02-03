package br.dev.brendo.agendaqui.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityOutputDTO {
    @Schema(example = "[8,9,10,11,14,15,16,17]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<Integer> possibleTimes = new ArrayList<>();
    @Schema(example = "[10,11]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<Integer> availableTimes = new ArrayList<>();
}
