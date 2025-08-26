package com.team5.graduation_project.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

//    @OneToMany(mappedBy = "medicine")
//    private List<MedicineStock> medicineStocks;
}