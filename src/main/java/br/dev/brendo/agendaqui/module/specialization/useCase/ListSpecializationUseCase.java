package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListSpecializationUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;

    public Page<SpecializationEntity> execute(Pageable page) {
        return this.specializationRepository.findAll(page);
    }
}
