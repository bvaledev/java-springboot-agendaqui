package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.patient.dto.PatientInputDTO;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import br.dev.brendo.agendaqui.module.specialization.useCase.UpdateSpecializationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UpdatePatientUseCase {
    private final PatientRepository patientRepository;

    @Autowired
    public UpdatePatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientEntity execute(String id, PatientInputDTO patientInputDTO) {
        var patient = this.patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        if (!Objects.equals(patient.getCpf(), patientInputDTO.cpf)){
            this.patientRepository.findByCpf(patientInputDTO.cpf).ifPresent(patientFound -> {
                throw new RuntimeException("Patient cpf already exists");
            });
            patient.setCpf(patientInputDTO.cpf);
        }
        if(!Objects.equals(patient.getEmail(), patientInputDTO.email)){
            this.patientRepository.findByEmail(patientInputDTO.email).ifPresent(patientFound -> {
                throw new RuntimeException("Patient email already exists");
            });
            patient.setEmail(patientInputDTO.email);
        }
        patient.setName(patientInputDTO.name);
        patient.setPhone(patientInputDTO.phone);
        patient.setBirthDate(patientInputDTO.birthDate);
        return this.patientRepository.save(patient);
    }
}
