package br.dev.brendo.agendaqui.module.appointment.useCase;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public ListAppointmentUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Page<AppointmentOutputDTO> execute(Pageable page) {
        return this.appointmentRepository.findAll(page).map(AppointmentOutputDTO::fromEntity);
    }
}
