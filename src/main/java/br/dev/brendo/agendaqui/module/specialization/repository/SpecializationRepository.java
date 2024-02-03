package br.dev.brendo.agendaqui.module.specialization.repository;

import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<SpecializationEntity, String> {
    Optional<SpecializationEntity> findBySlug(String slug);
    List<SpecializationEntity> findAllByEnabledTrue();
}
