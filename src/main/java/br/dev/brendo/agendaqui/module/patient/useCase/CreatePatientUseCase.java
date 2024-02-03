package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.patient.dto.PatientInputDTO;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreatePatientUseCase {
    private final PatientRepository patientRepository;

    @Autowired
    public CreatePatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientEntity execute(PatientInputDTO patientInputDTO) {
        this.patientRepository.findByCpf(patientInputDTO.cpf).ifPresent(patient -> {
            throw new RuntimeException("Patient cpf already exists");
        });
        this.patientRepository.findByEmail(patientInputDTO.email).ifPresent(patient -> {
            throw new RuntimeException("Patient email already exists");
        });
        var patient = PatientEntity.builder()
                .name(patientInputDTO.name)
                .cpf(patientInputDTO.cpf)
                .email(patientInputDTO.email)
                .phone(patientInputDTO.phone)
                .birthDate(patientInputDTO.birthDate)
                .build();
        return this.patientRepository.save(patient);
    }
}
