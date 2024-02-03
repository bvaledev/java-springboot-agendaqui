package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
