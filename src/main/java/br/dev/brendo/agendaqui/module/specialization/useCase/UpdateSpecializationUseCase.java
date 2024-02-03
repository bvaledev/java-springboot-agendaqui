package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.dto.SpecializationInputDTO;
import br.dev.brendo.agendaqui.module.specialization.dto.TimeIntervalInputDTO;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UpdateSpecializationUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    public SpecializationEntity execute(String id, SpecializationInputDTO updateSpecializationDTO) {
        var specialization = this.specializationRepository.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found"));
        specialization.setEnabled(updateSpecializationDTO.getEnabled());
        if (!Objects.equals(specialization.getName(), updateSpecializationDTO.getName())) {
            specialization.setName(updateSpecializationDTO.getName());
            specialization.setSlug();
            this.specializationRepository.findBySlug(specialization.getSlug()).ifPresent( spec -> {
                throw new RuntimeException("Specialization already exists");
            });
        }
        this.specializationRepository.save(specialization);
        this.updateTimeInterval(specialization, updateSpecializationDTO.getTimeIntervals());
        return specialization;
    }

    private void updateTimeInterval(SpecializationEntity specialization, List<TimeIntervalInputDTO> timeIntervalDTO ) {
        var timeIntervals = this.timeIntervalRepository.findAllBySpecializationId(specialization.getId());
        timeIntervals.forEach(timeInterval -> {
            timeIntervalDTO.stream()
                    .filter(dto -> dto.getWeekDay() == timeInterval.getWeekDay())
                    .findFirst()
                    .ifPresentOrElse(
                            dto -> {
                                timeInterval.setEnabled(true);
                                timeInterval.setTimeStartInMinutes(dto.getTimeStartInMinutes());
                                timeInterval.setTimeEndInMinutes(dto.getTimeEndInMinutes());
                            },
                            () -> {
                                timeInterval.setEnabled(false);
                            });
        });
        this.timeIntervalRepository.saveAll(timeIntervals);
    }
}
