package br.dev.brendo.agendaqui.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentInputDTO {
    @Schema(example = "2024-02-06T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDateTime date;
    @Schema(example = "Any note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String notes;
    @Schema(example = "medicina", requiredMode = Schema.RequiredMode.REQUIRED)
    public String specializationSlug;
    @UUID
    @Schema(example = "d9ceabf0-35d5-4435-8d2c-dde5a68ee2e0", requiredMode = Schema.RequiredMode.REQUIRED)
    public String patientId;
}
