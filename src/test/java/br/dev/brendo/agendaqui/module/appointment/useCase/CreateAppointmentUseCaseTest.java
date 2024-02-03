package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentInputDTO;
import br.dev.brendo.agendaqui.module.appointment.entity.AppointmentEntity;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private SpecializationRepository specializationRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Test
    public void should_throw_runtime_exception_when_date_is_before() {
        // Given
        LocalDateTime refDate = LocalDateTime.parse("1990-01-30T15:00:00");
        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        try{
            var patientId = UUID.randomUUID().toString();
            var createAppointmentDTO = new AppointmentInputDTO();
            createAppointmentDTO.setPatientId(patientId);
            createAppointmentDTO.setSpecializationSlug("medicina");
            createAppointmentDTO.setNotes("notes");
            createAppointmentDTO.setDate(refDate);
            // When
            createAppointmentUseCase.execute(createAppointmentDTO);

        }catch (RuntimeException e) {
            // Then
            assertEquals("Cannot create appointment in the past", e.getMessage());
        }
        mockedLocalDateTime.close();
    }

    @Test
    public void should_throw_runtime_exception_when_specialization_not_found() {
        // Given
        LocalDateTime refDate = LocalDateTime.parse("2024-01-30T15:00:00");
        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        var patientId = UUID.randomUUID().toString();
        var createAppointmentDTO = new AppointmentInputDTO();
        createAppointmentDTO.setPatientId(patientId);
        createAppointmentDTO.setSpecializationSlug("medicina");
        createAppointmentDTO.setNotes("notes");
        createAppointmentDTO.setDate(refDate);

        when(specializationRepository.findBySlug(createAppointmentDTO.getSpecializationSlug()))
                .thenReturn(Optional.empty());

        try{
            // When
            createAppointmentUseCase.execute(createAppointmentDTO);
        }catch (RuntimeException e) {
            // Then
            assertEquals("Specialization not found", e.getMessage());
        }
        mockedLocalDateTime.close();
    }

    @Test
    public void should_throw_runtime_exception_when_has_time_interval_conflict() {
        // Given
        LocalDateTime refDate = LocalDateTime.parse("2024-01-30T15:00:00");
        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        var patientId = UUID.randomUUID().toString();
        var createAppointmentDTO = new AppointmentInputDTO();
        createAppointmentDTO.setPatientId(patientId);
        createAppointmentDTO.setSpecializationSlug("medicina");
        createAppointmentDTO.setNotes("notes");
        createAppointmentDTO.setDate(refDate);

        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .build();
        var appointment = new AppointmentEntity();

        when(specializationRepository.findBySlug(createAppointmentDTO.getSpecializationSlug()))
                .thenReturn(Optional.of(specialization));
        when(appointmentRepository.findBySpecializationIdAndDate(specializationUUID, refDate))
                .thenReturn(Optional.of(appointment));

        try{
            // When
            createAppointmentUseCase.execute(createAppointmentDTO);
        }catch (RuntimeException e) {
            // Then
            assertEquals("There is another appointment in this date and time", e.getMessage());
        }
        mockedLocalDateTime.close();
    }

    @Test
    public void should_throw_runtime_exception_when_patient_not_exists() {
        // Given
        LocalDateTime refDate = LocalDateTime.parse("2024-01-30T15:00:00");
        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        var patientId = UUID.randomUUID().toString();
        var createAppointmentDTO = new AppointmentInputDTO();
        createAppointmentDTO.setPatientId(patientId);
        createAppointmentDTO.setSpecializationSlug("medicina");
        createAppointmentDTO.setNotes("notes");
        createAppointmentDTO.setDate(refDate);

        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .build();

        when(specializationRepository.findBySlug(createAppointmentDTO.getSpecializationSlug()))
                .thenReturn(Optional.of(specialization));
        when(appointmentRepository.findBySpecializationIdAndDate(specializationUUID, refDate))
                .thenReturn(Optional.empty());
        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        try{
            // When
            createAppointmentUseCase.execute(createAppointmentDTO);
        }catch (RuntimeException e) {
            // Then
            assertEquals("Patient not found", e.getMessage());
        }
        mockedLocalDateTime.close();
    }

    @Test
    public void should_create_appointment() {
        // Given
        LocalDateTime refDate = LocalDateTime.parse("2024-01-30T15:00:00");
        LocalDateTime fixedDate = LocalDateTime.parse("2024-01-30T15:00:00");
        var mockedLocalDateTime = mockStatic(LocalDateTime.class);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDate);

        var patientId = UUID.randomUUID().toString();
        var patient = new PatientEntity();
        var createAppointmentDTO = new AppointmentInputDTO();
        createAppointmentDTO.setPatientId(patientId);
        createAppointmentDTO.setSpecializationSlug("medicina");
        createAppointmentDTO.setNotes("notes");
        createAppointmentDTO.setDate(refDate);

        var specializationUUID = UUID.randomUUID().toString();
        var specialization = SpecializationEntity.builder()
                .id(specializationUUID)
                .enabled(true)
                .name("Medicina")
                .slug("medicina")
                .build();
        var appointment = new AppointmentEntity();

        when(specializationRepository.findBySlug(createAppointmentDTO.getSpecializationSlug()))
                .thenReturn(Optional.of(specialization));
        when(appointmentRepository.findBySpecializationIdAndDate(specializationUUID, refDate))
                .thenReturn(Optional.empty());
        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));
        when(appointmentRepository.save(Mockito.any(AppointmentEntity.class))).thenReturn(appointment);

        // When
        var response = createAppointmentUseCase.execute(createAppointmentDTO);
        // Then
        assertNotNull(response);

        mockedLocalDateTime.close();
    }
}
