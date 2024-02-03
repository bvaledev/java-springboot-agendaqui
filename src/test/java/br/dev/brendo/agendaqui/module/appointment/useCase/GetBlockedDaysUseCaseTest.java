package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetBlockedDaysUseCaseTest {
    @Mock
    private TimeIntervalRepository timeIntervalRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private GetBlockedDaysUseCase getBlockedDaysUseCase;

    @Test
    public void should_return_error_when_month_is_invalid(){
        // Given
        String specializationSlug = "medicina";
        Integer month = 0;
        Integer year = 2024;
        try{
            // When
            getBlockedDaysUseCase.execute(specializationSlug, month, year);
        }catch (RuntimeException e) {
            // Then
            assertEquals("Invalid month or year", e.getMessage());
        }
    }

    @Test
    public void should_return_error_when_year_is_invalid(){
        // Given
        String specializationSlug = "medicina";
        Integer month = 1;
        Integer year = 2000;
        try{
            // When
            getBlockedDaysUseCase.execute(specializationSlug, month, year);
        }catch (RuntimeException e) {
            // Then
            assertEquals("Invalid month or year", e.getMessage());
        }
    }

    @Test
    public void should_return_blocked_days(){
        // Given
        String specializationSlug = "medicina";
        Integer month = 1;
        Integer year = LocalDateTime.now().getYear();

        var timeInterval = TimeIntervalEntity.builder()
                .weekDay(4)
                .timeStartInMinutes(480)
                .timeEndInMinutes(1080)
                .build();
        var availableIntervals = List.of(timeInterval);
        var blockedDays = List.of(10,30);

        when(this.timeIntervalRepository.findAllBySpecializationSlugAndEnabledTrue(Mockito.any())).thenReturn(availableIntervals);
        when(this.appointmentRepository.findBlockedDays(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(blockedDays);

        // When
        var response = getBlockedDaysUseCase.execute(specializationSlug, month, year);

        // Then
        assertNotNull(response);
        assertEquals(List.of(1, 2, 3, 5, 6, 7), response.getBlockedWeekDays());
        assertEquals(List.of(10, 30), response.getBlockedDates());
    }

}
