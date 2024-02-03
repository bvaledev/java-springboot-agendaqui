package br.dev.brendo.agendaqui.module.specialization.controller;

import br.dev.brendo.agendaqui.module.pagination.PaginatedOutputDTO;
import br.dev.brendo.agendaqui.module.specialization.dto.SpecializationInputDTO;
import br.dev.brendo.agendaqui.module.specialization.dto.SpecializationOutputDTO;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.useCase.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialization")
@Tag(name = "Specialization", description = "Specialization API")
public class SpecializationController {
    private final ListSpecializationUseCase listSpecializationUseCase;
    private final ListSpecializationEnabledUseCase listSpecializationEnabledUseCase;
    private final GetSpecializationTimeIntervalUseCase getSpecializationTimeIntervalUseCase;
    private final CreateSpecializationUseCase createSpecializationUseCase;
    private final UpdateSpecializationUseCase updateSpecializationUseCase;
    private final DisableSpecializationUseCase disableSpecializationUseCase;

    @Autowired
    public SpecializationController(ListSpecializationUseCase listSpecializationUseCase, ListSpecializationEnabledUseCase listSpecializationEnabledUseCase, GetSpecializationTimeIntervalUseCase getSpecializationTimeIntervalUseCase, CreateSpecializationUseCase createSpecializationUseCase, UpdateSpecializationUseCase updateSpecializationUseCase, DisableSpecializationUseCase disableSpecializationUseCase) {
        this.listSpecializationUseCase = listSpecializationUseCase;
        this.listSpecializationEnabledUseCase = listSpecializationEnabledUseCase;
        this.getSpecializationTimeIntervalUseCase = getSpecializationTimeIntervalUseCase;
        this.createSpecializationUseCase = createSpecializationUseCase;
        this.updateSpecializationUseCase = updateSpecializationUseCase;
        this.disableSpecializationUseCase = disableSpecializationUseCase;
    }

    @GetMapping()
    public ResponseEntity<PaginatedOutputDTO<SpecializationOutputDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<SpecializationEntity> pageSpecializations = this.listSpecializationUseCase.execute(paging);
        List<SpecializationOutputDTO> specializations = SpecializationOutputDTO.FromSpecializationList(pageSpecializations.getContent());
        var result = new PaginatedOutputDTO<SpecializationOutputDTO>(specializations, paging);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<SpecializationOutputDTO>> listEnabled() {
        var specializations = this.listSpecializationEnabledUseCase.execute();
        return ResponseEntity.ok().body(SpecializationOutputDTO.FromSpecializationList(specializations));
    }

    @GetMapping("/{id}/time-intervals")
    public ResponseEntity<Object> getSpecializationTimeIntervals(@PathVariable("id") String id) {
        var timeIntervals = this.getSpecializationTimeIntervalUseCase.execute(id);
        return ResponseEntity.ok().body(timeIntervals);
    }

    @PostMapping()
    public ResponseEntity<SpecializationOutputDTO> create(@Valid @RequestBody SpecializationInputDTO createSpecializationDTO) {
        var specialization = this.createSpecializationUseCase.execute(createSpecializationDTO);
        return ResponseEntity.ok().body(SpecializationOutputDTO.FromSpecialization(specialization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationOutputDTO> update(@PathVariable("id") String id, @Valid @RequestBody SpecializationInputDTO updateSpecializationDTO) {
        var specialization = this.updateSpecializationUseCase.execute(id, updateSpecializationDTO);
        return ResponseEntity.ok().body(SpecializationOutputDTO.FromSpecialization(specialization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disable(@PathVariable("id") String id) {
        this.disableSpecializationUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
