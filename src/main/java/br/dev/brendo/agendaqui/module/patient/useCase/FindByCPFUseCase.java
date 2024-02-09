package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindByCPFUseCase {
    private final PatientRepository patientRepository;

    @Autowired
    public FindByCPFUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientEntity execute(String cpf) {
        return this.patientRepository.findByCpf(cpf).orElseThrow(()-> new RuntimeException("Patient not found"));
    }
}
