package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListPatientUseCase {
    private final PatientRepository patientRepository;

    @Autowired
    public ListPatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Page<PatientEntity> execute(Pageable page, Optional<String> cpf) {
        if (cpf.isPresent()) {
            return this.patientRepository.findAllByCpf(cpf.get(), page);
        }
        return this.patientRepository.findAll(page);
    }
}
