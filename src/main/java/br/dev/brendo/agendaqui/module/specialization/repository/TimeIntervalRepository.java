package br.dev.brendo.agendaqui.module.specialization.repository;

import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeIntervalRepository extends JpaRepository<TimeIntervalEntity, String>{
    List<TimeIntervalEntity>  findAllBySpecializationId(String specializationId);
    List<TimeIntervalEntity> findAllBySpecializationIdAndEnabledTrue(String specializationId);
    List<TimeIntervalEntity> findAllBySpecializationSlugAndEnabledTrue(String specializationSlug);
}
