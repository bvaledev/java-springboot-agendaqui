package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentInputDTO;
import br.dev.brendo.agendaqui.module.appointment.entity.AppointmentEntity;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final SpecializationRepository specializationRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public CreateAppointmentUseCase(AppointmentRepository appointmentRepository, SpecializationRepository specializationRepository , PatientRepository patientRepository ) {
        this.appointmentRepository = appointmentRepository;
        this.specializationRepository = specializationRepository;
        this.patientRepository = patientRepository;
    }

    public AppointmentEntity execute(AppointmentInputDTO appointmentInputDTO) {
        LocalDateTime appointmentDate = appointmentInputDTO.getDate().withMinute(0).withSecond(0);
        if(appointmentDate.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Cannot create appointment in the past");
        }

        var specialization = this.specializationRepository.findBySlug(appointmentInputDTO.getSpecializationSlug())
                .orElseThrow(() -> new RuntimeException("Specialization not found"));

        this.appointmentRepository.findBySpecializationIdAndDate(specialization.getId(), appointmentDate)
               .ifPresent(appointment -> {
                    throw new RuntimeException("There is another appointment in this date and time");
                });

        var patient = this.patientRepository.findById(appointmentInputDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        var appointment = AppointmentEntity.builder()
                .specialization(specialization)
                .notes(appointmentInputDTO.getNotes())
                .patient(patient)
                .date(appointmentDate)
                .build();

        return this.appointmentRepository.save(appointment);
    }
}
