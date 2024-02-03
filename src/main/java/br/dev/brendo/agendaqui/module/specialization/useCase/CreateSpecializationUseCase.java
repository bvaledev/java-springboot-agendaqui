package br.dev.brendo.agendaqui.module.specialization.useCase;

import br.dev.brendo.agendaqui.module.specialization.dto.SpecializationInputDTO;
import br.dev.brendo.agendaqui.module.specialization.dto.TimeIntervalInputDTO;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CreateSpecializationUseCase {
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    public SpecializationEntity execute(SpecializationInputDTO createSpecializationDTO) {
        var specialization = SpecializationEntity.builder()
                .name(createSpecializationDTO.name)
                .enabled(createSpecializationDTO.enabled)
                .build();
        specialization.setSlug();
        this.specializationRepository.findBySlug(specialization.getSlug()).ifPresent( spec -> {
            throw new RuntimeException("Specialization already exists");
        });
        specialization = this.specializationRepository.save(specialization);
        this.generateTimeInterval(specialization, createSpecializationDTO.getTimeIntervals());
        return specialization;
    }

    private void generateTimeInterval(SpecializationEntity specialization, List<TimeIntervalInputDTO> timeIntervalDTO ) {
        List<TimeIntervalEntity> defaultTimeIntervals = new ArrayList<>(
                Arrays.asList(
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(1).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(2).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(3).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(4).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(5).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(6).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build(),
                        TimeIntervalEntity.builder().specialization(specialization).weekDay(7).timeStartInMinutes(8*60).timeEndInMinutes(18*60).build()
                )
        );

        defaultTimeIntervals.forEach(timeInterval -> {
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

        timeIntervalRepository.saveAll(defaultTimeIntervals);
    }
}
