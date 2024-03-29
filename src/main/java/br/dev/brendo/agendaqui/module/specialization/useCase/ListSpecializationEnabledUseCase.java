package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSpecializationEnabledUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;

    public List<SpecializationEntity> execute() {
        return this.specializationRepository.findAllByEnabledTrue();
    }
}
