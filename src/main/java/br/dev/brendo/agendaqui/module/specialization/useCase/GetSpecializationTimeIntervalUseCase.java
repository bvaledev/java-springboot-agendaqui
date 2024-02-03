package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSpecializationTimeIntervalUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    public List<TimeIntervalEntity> execute(String id) {
        this.specializationRepository.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found"));
        return this.timeIntervalRepository.findAllBySpecializationIdAndEnabledTrue(id);
    }
}
