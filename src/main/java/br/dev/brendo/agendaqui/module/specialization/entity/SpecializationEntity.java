package br.dev.brendo.agendaqui.module.specialization.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "specialization")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecializationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;
    @CreationTimestamp()
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp()
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TimeIntervalEntity> timeIntervals;

    public void setName(String name) {
        this.name = name;
        this.setSlug(name);
    }

    public void setSlug() {
        this.slug = this.name.toLowerCase().replaceAll(" ", "-");
    }
}
