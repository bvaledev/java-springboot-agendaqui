package br.dev.brendo.agendaqui.module.specialization.dto;

import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationOutputDTO {
    @Schema(example = "d9ceabf0-35d5-4435-8d2c-dde5a68ee2e0", requiredMode = Schema.RequiredMode.REQUIRED)
    public String id;
    @Schema(example = "Medicina", requiredMode = Schema.RequiredMode.REQUIRED)
    public String name;
    @Schema(example = "medicina", requiredMode = Schema.RequiredMode.REQUIRED)
    public String slug;
    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    public boolean enabled;
    @Schema(example = "2024-02-06T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDateTime createdAt;
    @Schema(example = "2024-02-06T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDateTime updatedAt;

    public static SpecializationOutputDTO FromSpecialization(SpecializationEntity specialization) {
        return new SpecializationOutputDTO(
                specialization.getId(),
                specialization.getName(),
                specialization.getSlug(),
                specialization.isEnabled(),
                specialization.getCreatedAt(),
                specialization.getUpdatedAt()
        );
    }

    public static List<SpecializationOutputDTO> FromSpecializationList(List<SpecializationEntity> specializations) {
        return specializations.stream().map(SpecializationOutputDTO::FromSpecialization).toList();
    }
}
