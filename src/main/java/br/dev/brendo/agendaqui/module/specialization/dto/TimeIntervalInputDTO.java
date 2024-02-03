package br.dev.brendo.agendaqui.module.specialization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeIntervalInputDTO {
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1)
    @Max(value = 7)
    public int weekDay;
    @Schema(example = "480", requiredMode = Schema.RequiredMode.REQUIRED)
    public int timeStartInMinutes;
    @Schema(example = "1080", requiredMode = Schema.RequiredMode.REQUIRED)
    public int timeEndInMinutes;
}
