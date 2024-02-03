package br.dev.brendo.agendaqui.module.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientInputDTO {
    @Schema(example = "Fulano", requiredMode = Schema.RequiredMode.REQUIRED)
    public String name;
    @Schema(example = "12345678900", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[0-9]{11}$", message = "O campo (cpf) deve conter 11 dígitos")
    public String cpf;
    @Schema(example = "email@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "O campo (email) deve ser um email válido")
    public String email;
    @Schema(example = "(99) 99999-9999", requiredMode = Schema.RequiredMode.REQUIRED)
    public String phone;
    @Schema(example = "1999-12-31", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDate birthDate;
}
