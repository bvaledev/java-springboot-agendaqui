package br.dev.brendo.agendaqui.module.specialization.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "time_intervals")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeIntervalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne()
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    @JsonIgnore
    private SpecializationEntity specialization;
    private int weekDay;
    private int timeStartInMinutes;
    private int timeEndInMinutes;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;
}
