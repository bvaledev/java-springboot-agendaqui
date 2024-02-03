package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GetTimeIntervalAvailabilityUseCaseTest {
    @Mock
    private SpecializationRepository specializationRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private GetTimeIntervalAvailabilityUseCase getTimeIntervalAvailabilityUseCase;

    @Test
    void should_remove_blocked_days() {
        // Given
        var timeInterval = TimeIntervalEntity.builder()
                .weekDay(2)
                .timeStartInMinutes(480)
                .timeEndInMinutes(1080)
                .enabled(true)
                .build();
        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .timeIntervals(new ArrayList<>(Collections.singletonList(timeInterval)))
                .build();

        var dateRef = LocalDateTime.parse("2024-01-30T16:00:00");
        var blockedDays = new ArrayList<>(Arrays.asList(dateRef));

        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        when(specializationRepository.findBySlug("medicina")).thenReturn(Optional.of(specialization));
        when(appointmentRepository.findBlockedTimes(specializationUUID, 480/60, 1080/60, 30, 1, 2024)).thenReturn(blockedDays);

        // When
        var response = getTimeIntervalAvailabilityUseCase.execute("medicina", dateRef);

        // Then
        assertEquals(Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15, 16, 17), response.getPossibleTimes());
        assertEquals(Arrays.asList(15, 17), response.getAvailableTimes());

        mockedLocalDateTime.close();
    }

    @Test
    void should_return_all_available_times() {
        // Given
        var timeInterval = TimeIntervalEntity.builder()
                .weekDay(2)
                .timeStartInMinutes(480)
                .timeEndInMinutes(1080)
                .enabled(true)
                .build();
        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .timeIntervals(new ArrayList<>(Collections.singletonList(timeInterval)))
                .build();

        var dateRef = LocalDateTime.parse("2024-01-30T00:00:00");
        var blockedDays = new ArrayList<>(List.of(dateRef));

        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        when(specializationRepository.findBySlug("medicina")).thenReturn(Optional.of(specialization));
        when(appointmentRepository.findBlockedTimes(specializationUUID, 480/60, 1080/60, 30, 1, 2024)).thenReturn(blockedDays);

        // When
        var response = getTimeIntervalAvailabilityUseCase.execute("medicina", dateRef);

        // Then
        assertEquals(Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15, 16, 17), response.getPossibleTimes());
        assertEquals(Arrays.asList(15, 16, 17), response.getAvailableTimes());

        mockedLocalDateTime.close();
    }

    @Test
    public void should_return_empty_available_times_when_reference_date_is_before(){
        // Given
        var dateRef = LocalDateTime.parse("1990-01-01T00:00:00");

        // When
        var response = getTimeIntervalAvailabilityUseCase.execute("medicina", dateRef);

        // Then
        assertEquals(List.of(), response.getPossibleTimes());
        assertEquals(List.of(), response.getAvailableTimes());
    }

    @Test
    public void should_throw_runtime_exception_when_specialization_not_exists(){
        try{
            // Given
            var dateRef = LocalDateTime.parse("1990-01-01T00:00:00");
            // When
            getTimeIntervalAvailabilityUseCase.execute("not_found", dateRef);
        }catch (RuntimeException e){
            // Then
            assertEquals("Specialization not found", e.getMessage());
        }
    }

    @Test
    public void should_return_empty_available_times_when_specialization_time_interval_null(){
        // Given
        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .build();
        var dateRef = LocalDateTime.parse("2024-01-30T00:00:00");

        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        when(specializationRepository.findBySlug("medicina")).thenReturn(Optional.of(specialization));

        // When
        var response = getTimeIntervalAvailabilityUseCase.execute("medicina", dateRef);

        // Then
        assertEquals(List.of(), response.getPossibleTimes());
        assertEquals(List.of(), response.getAvailableTimes());

        mockedLocalDateTime.close();
    }

    @Test
    public void should_return_empty_available_times_when_specialization_time_interval_empty(){
        // Given
        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .timeIntervals(new ArrayList<>())
                .name("Medicina")
                .slug("medicina")
                .build();

        var dateRef = LocalDateTime.parse("2024-01-30T00:00:00");

        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        when(specializationRepository.findBySlug("medicina")).thenReturn(Optional.of(specialization));

        // When
        var response = getTimeIntervalAvailabilityUseCase.execute("medicina", dateRef);

        // Then
        assertEquals(List.of(), response.getPossibleTimes());
        assertEquals(List.of(), response.getAvailableTimes());

        mockedLocalDateTime.close();
    }
}
