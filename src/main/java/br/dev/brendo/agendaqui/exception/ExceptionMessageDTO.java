package br.dev.brendo.agendaqui.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMessageDTO {
    @Schema(example = "Any message", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}