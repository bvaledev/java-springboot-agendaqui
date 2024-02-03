package br.dev.brendo.agendaqui.module.patient.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAppointmentsByCPFUseCase {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public FindAppointmentsByCPFUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<AppointmentOutputDTO> execute(String cpf) {
        return this.appointmentRepository.findAllByPatientCpf(cpf).stream().map(AppointmentOutputDTO::fromEntity).toList();
    }
}
