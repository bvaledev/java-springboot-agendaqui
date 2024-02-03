package br.dev.brendo.agendaqui.module.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="patient")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(unique = true, length = 11)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String phone;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @CreationTimestamp()
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp()
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
