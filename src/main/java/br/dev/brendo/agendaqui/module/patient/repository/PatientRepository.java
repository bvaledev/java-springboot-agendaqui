package br.dev.brendo.agendaqui.module.patient.repository;

import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    Optional<PatientEntity> findByCpf(String cpf);
    Optional<PatientEntity> findByEmail(String cpf);

    Page<PatientEntity> findAllByCpf(String cpf, Pageable pageable);
}
