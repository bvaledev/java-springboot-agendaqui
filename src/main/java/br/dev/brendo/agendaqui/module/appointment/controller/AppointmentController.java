package br.dev.brendo.agendaqui.module.appointment.controller;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentInputDTO;
import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.dto.AvailabilityOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.dto.BlockedDaysOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.useCase.CreateAppointmentUseCase;
import br.dev.brendo.agendaqui.module.appointment.useCase.GetBlockedDaysUseCase;
import br.dev.brendo.agendaqui.module.appointment.useCase.GetTimeIntervalAvailabilityUseCase;
import br.dev.brendo.agendaqui.module.appointment.useCase.ListAppointmentUseCase;
import br.dev.brendo.agendaqui.module.pagination.PaginatedOutputDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Appointments API")
public class AppointmentController {
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final GetBlockedDaysUseCase getBlockedDaysUseCase;
    private final GetTimeIntervalAvailabilityUseCase getTimeIntervalAvailabilityUseCase;
    private final ListAppointmentUseCase listAppointmentUseCase;

    @Autowired
    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase, GetBlockedDaysUseCase getBlockedDaysUseCase, GetTimeIntervalAvailabilityUseCase getTimeIntervalAvailabilityUseCase, ListAppointmentUseCase listAppointmentUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.getBlockedDaysUseCase = getBlockedDaysUseCase;
        this.getTimeIntervalAvailabilityUseCase = getTimeIntervalAvailabilityUseCase;
        this.listAppointmentUseCase = listAppointmentUseCase;
    }

    @GetMapping()
    public ResponseEntity<PaginatedOutputDTO<AppointmentOutputDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<AppointmentOutputDTO> pageAppointments = listAppointmentUseCase.execute(paging);
        var result = new PaginatedOutputDTO<AppointmentOutputDTO>(pageAppointments.getContent(), paging);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping()
    public ResponseEntity<AppointmentOutputDTO> createAppointment(@Valid @RequestBody() AppointmentInputDTO appointmentInputDTO) {
        var appointment = this.createAppointmentUseCase.execute(appointmentInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentOutputDTO.fromEntity(appointment));
    }

    @GetMapping("/blocked-days")
    public ResponseEntity<BlockedDaysOutputDTO> getBlockedDays(
            @RequestParam("specializationSlug") String specializationSlug,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year
    ) {
        var blockedDays = this.getBlockedDaysUseCase.execute(specializationSlug, month, year);
        return ResponseEntity.ok().body(blockedDays);
    }

    @GetMapping("/available-time-intervals")
    public ResponseEntity<AvailabilityOutputDTO> getAvailableTimeIntervals(
            @RequestParam("specializationSlug") String specializationSlug,
            @RequestParam("dateTime") String dateTime
    ) {
        var refDate = LocalDateTime.parse(dateTime);
        var availability = this.getTimeIntervalAvailabilityUseCase.execute(specializationSlug, refDate);
        return ResponseEntity.ok().body(availability);
    }
}
