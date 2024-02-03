package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.AvailabilityOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetTimeIntervalAvailabilityUseCase {
    private final SpecializationRepository specializationRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public GetTimeIntervalAvailabilityUseCase(SpecializationRepository specializationRepository, AppointmentRepository appointmentRepository) {
        this.specializationRepository = specializationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public AvailabilityOutputDTO execute(String specializationSlug, LocalDateTime referenceDateTime) {
        LocalDateTime endOfDay = referenceDateTime.withHour(23).withMinute(59).withSecond(59);
        if(endOfDay.isBefore(LocalDateTime.now())){
            return new AvailabilityOutputDTO();
        }

        var specialization = this.specializationRepository.findBySlug(specializationSlug)
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        if(specialization.getTimeIntervals() == null){
            return new AvailabilityOutputDTO();
        }

        var timeIntervalAvailable = specialization.getTimeIntervals()
                .stream()
                .filter(timeInterval -> timeInterval.getWeekDay() == referenceDateTime.getDayOfWeek().getValue())
                .filter(TimeIntervalEntity::isEnabled)
                .findFirst();

        if(timeIntervalAvailable.isEmpty()){
            return new AvailabilityOutputDTO();
        }

        var timeStartInMinutes = timeIntervalAvailable.get().getTimeStartInMinutes();
        var timeEndInMinutes = timeIntervalAvailable.get().getTimeEndInMinutes();
        var consultationTimeInMinutes = 60;
        var startInHour = timeStartInMinutes / consultationTimeInMinutes;
        var endInHour = timeEndInMinutes / consultationTimeInMinutes;

        List<Integer> possibleTimes = IntStream.range(0, endInHour-startInHour)
                .map(i -> startInHour + i)
                .boxed()
                .toList();

        List<LocalDateTime> blockedTimes = this.appointmentRepository.findBlockedTimes(
                        specialization.getId(),
                        startInHour,
                        endInHour,
                        referenceDateTime.getDayOfMonth(),
                        referenceDateTime.getMonthValue(),
                        referenceDateTime.getYear()
                );

        var availableTimes = possibleTimes.stream()
                .filter(time -> {
                    var isBlocked = blockedTimes.stream().anyMatch(blocked -> blocked.getHour() == time);
                    var isPast = referenceDateTime.withHour(time).isBefore(LocalDateTime.now());
                    return !isPast && !isBlocked;
                });

        return AvailabilityOutputDTO.builder()
                .availableTimes(availableTimes.collect(Collectors.toList()))
                .possibleTimes(possibleTimes)
                .build();
    }
}
