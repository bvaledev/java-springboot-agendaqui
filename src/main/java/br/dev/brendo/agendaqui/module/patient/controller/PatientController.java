package br.dev.brendo.agendaqui.module.patient.controller;

import br.dev.brendo.agendaqui.module.appointment.dto.AppointmentOutputDTO;
import br.dev.brendo.agendaqui.module.pagination.PaginatedOutputDTO;
import br.dev.brendo.agendaqui.module.patient.dto.PatientInputDTO;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.useCase.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@Tag(name = "Patient", description = "Patient API")
public class PatientController {
    private final ListPatientUseCase listPatientUseCase;
    private final CreatePatientUseCase createPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final FindByCPFUseCase findByCPFUseCase;
    private final FindAppointmentsByCPFUseCase findAppointmentsByCPFUseCase;

    @Autowired
    public PatientController(ListPatientUseCase listPatientUseCase, CreatePatientUseCase createPatientUseCase, UpdatePatientUseCase updatePatientUseCase, FindByCPFUseCase findByCPFUseCase, FindAppointmentsByCPFUseCase findAppointmentsByCPFUseCase) {
        this.listPatientUseCase = listPatientUseCase;
        this.createPatientUseCase = createPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.findByCPFUseCase = findByCPFUseCase;
        this.findAppointmentsByCPFUseCase = findAppointmentsByCPFUseCase;
    }

    @GetMapping()
    public ResponseEntity<PaginatedOutputDTO<PatientEntity>> list(
            @RequestParam(required = false) String cpf,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<PatientEntity> pagePatients = listPatientUseCase.execute(paging, Optional.ofNullable(cpf));
        var result = new PaginatedOutputDTO<PatientEntity>(pagePatients.getContent(), paging);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping()
    public ResponseEntity<PatientEntity> create(@Validated @RequestBody PatientInputDTO createPatientDTO) {
        var patient = createPatientUseCase.execute(createPatientDTO);
        return ResponseEntity.ok().body(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientEntity> update(@PathVariable String id, @Validated @RequestBody PatientInputDTO updatePatientDTO) {
        var patient = updatePatientUseCase.execute(id, updatePatientDTO);
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PatientEntity> findByCPF(@PathVariable String cpf) {
        var response = findByCPFUseCase.execute(cpf);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{cpf}/appointments")
    public ResponseEntity<List<AppointmentOutputDTO>> findAppointmentsByCPF(@PathVariable String cpf) {
        var response = findAppointmentsByCPFUseCase.execute(cpf);
        return ResponseEntity.ok().body(response);
    }
}
