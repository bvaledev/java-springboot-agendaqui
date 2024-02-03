package br.dev.brendo.agendaqui.module.specialization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationInputDTO {
    @Schema(example = "Medicina", requiredMode = Schema.RequiredMode.REQUIRED)
    public String name;
    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    public Boolean enabled = true;
    @Schema(example = "[]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<TimeIntervalInputDTO> timeIntervals;

    public SpecializationInputDTO(String name){
        this.name = name;

        var interval = new TimeIntervalInputDTO();
        interval.weekDay = 2;
        interval.timeStartInMinutes = 480;
        interval.timeEndInMinutes = 1080;
        this.timeIntervals = new ArrayList<>();
        this.timeIntervals.add(interval);
    }
}
