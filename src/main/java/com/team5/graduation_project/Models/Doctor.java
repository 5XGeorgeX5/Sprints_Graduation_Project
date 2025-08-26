package com.team5.graduation_project.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    private LocalTime startShift;
    private LocalTime endShift;
    private Integer appointmentDuration;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}