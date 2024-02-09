package br.dev.brendo.agendaqui.module.appointment.dto;

import br.dev.brendo.agendaqui.module.appointment.entity.AppointmentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentOutputDTO {
    @Schema(example = "d9ceabf0-35d5-4435-8d2c-dde5a68ee2e0", requiredMode = Schema.RequiredMode.REQUIRED)
    public String id;
    @Schema(example = "QUEUE", requiredMode = Schema.RequiredMode.REQUIRED)
    public String status;
    @Schema(example = "2024-02-06T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDateTime date;
    @Schema(example = "Any note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String notes;
    @Schema(example = "Medicina", requiredMode = Schema.RequiredMode.REQUIRED)
    public String specialization;
    @Schema(example = "Fulano", requiredMode = Schema.RequiredMode.REQUIRED)
    public String patient;

    public static AppointmentOutputDTO fromEntity(AppointmentEntity appointment) {
        return AppointmentOutputDTO.builder()
                .id(appointment.getId())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .date(appointment.getDate())
                .specialization(appointment.getSpecialization().getName())
                .patient(appointment.getPatient().getName())
                .build();
    }
}
