package br.dev.brendo.agendaqui.module.appointment.entity;

import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "appointment")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT 'QUEUE'")
    private String status;

    @Column(nullable = true)
    private String notes;

    @Column()
    private LocalDateTime date;

    @ManyToOne()
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    // @JsonIgnore
    private SpecializationEntity specialization;

    @ManyToOne()
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    // @JsonIgnore
    private PatientEntity patient;

    @PrePersist
    void preInsert() {
        if (this.status == null)
            this.status = "QUEUE";
    }

    public void setStatusQueue() {
        this.status = "QUEUE";
    }

    public void setStatusCanceled() {
        this.status = "CANCELED";
    }

    public void setStatusConfirmed() {
        this.status = "CONFIRMED";
    }

    public void setStatusAbsent() {
        this.status = "ABSENT";
    }

    public void setStatusTreated() {
        this.status = "TREATED";
    }
}
