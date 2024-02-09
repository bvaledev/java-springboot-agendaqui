package br.dev.brendo.agendaqui.module.appointment.repository;

import br.dev.brendo.agendaqui.module.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    Optional<AppointmentEntity> findBySpecializationIdAndDate(String id, LocalDateTime date);
    List<AppointmentEntity> findAllByPatientCpf(String cpf);

    @Query(value = "SELECT a.date FROM appointment a " +
            "WHERE EXTRACT(HOUR FROM a.date) >= :startInHour " +
            "AND EXTRACT(HOUR FROM a.date) <= :endInHour " +
            "AND EXTRACT(DAY FROM a.date) = :day " +
            "AND EXTRACT(MONTH FROM a.date) = :month " +
            "AND EXTRACT(YEAR FROM a.date) = :year " +
            "AND EXTRACT(YEAR FROM a.date) = :year " +
            "AND a.specialization.id = :specializationId")
    List<LocalDateTime> findBlockedTimes(String specializationId, int startInHour, int endInHour, int day, int month, int year);

    @Query("SELECT DAY(a.date) AS DATE FROM appointment a " +
            "LEFT JOIN time_intervals ti ON ti.weekDay = FUNCTION('DATE_PART', 'DOW', a.date) " +
            "AND ti.specialization.id = a.specialization.id " +
            "WHERE a.specialization.slug = :specializationSlug " +
            "AND MONTH(a.date) = :month " +
            "AND YEAR(a.date) = :year " +
            "GROUP BY DAY(a.date), ((ti.timeEndInMinutes - ti.timeStartInMinutes) / 60)" +
            "HAVING COUNT(a.date) >= ((ti.timeEndInMinutes - ti.timeStartInMinutes) / 60)")
    List<Integer> findBlockedDays(String specializationSlug, Integer month, Integer year);


    Optional<AppointmentEntity> findAllBySpecializationId(String specializationId);
    Optional<AppointmentEntity> findBySpecializationId(String specializationId);
}
