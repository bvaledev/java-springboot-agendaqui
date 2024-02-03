package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisableSpecializationUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;

    public void execute(String id) {
        var specialization = this.specializationRepository.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found"));
        specialization.setEnabled(false);
        this.specializationRepository.save(specialization);
    }
}
