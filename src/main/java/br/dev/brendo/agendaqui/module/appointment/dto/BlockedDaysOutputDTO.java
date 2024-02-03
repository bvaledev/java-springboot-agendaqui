package br.dev.brendo.agendaqui.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedDaysOutputDTO {
    @Schema(example = "[1,3,4,6,7]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<Integer> blockedWeekDays;
    @Schema(example = "[10,12,15]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<Integer> blockedDates;
}
