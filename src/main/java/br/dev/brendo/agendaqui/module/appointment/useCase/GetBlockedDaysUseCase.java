package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.BlockedDaysOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GetBlockedDaysUseCase {
    private final AppointmentRepository appointmentRepository;
    private final TimeIntervalRepository timeIntervalRepository;

    @Autowired
    public GetBlockedDaysUseCase(AppointmentRepository appointmentRepository, TimeIntervalRepository timeIntervalRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeIntervalRepository = timeIntervalRepository;
    }

    public BlockedDaysOutputDTO execute(String specializationSlug, Integer month, Integer year) {
        LocalDateTime today = LocalDateTime.now();
        if ((+month < 1 && +month > 12) || +year < today.getYear()) {
            throw new RuntimeException("Invalid month or year");
        }

        var availableWeekDays = this.timeIntervalRepository.findAllBySpecializationSlugAndEnabledTrue(specializationSlug);

        var weekDays = List.of(1,2,3,4,5,6,7);

        var blockedWeekDays = weekDays.stream()
                .filter(weekDay -> availableWeekDays.stream().noneMatch(availableWeekDay ->
                        availableWeekDay.getWeekDay() == weekDay)
                ).toList();

        var blockedDatesRaw = this.appointmentRepository.findBlockedDays(specializationSlug, month, year);

        return BlockedDaysOutputDTO.builder().blockedWeekDays(blockedWeekDays).blockedDates(blockedDatesRaw).build();
    }
}
