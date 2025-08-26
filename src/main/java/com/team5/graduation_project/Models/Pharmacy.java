package com.team5.graduation_project.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "pharmacy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pharmacy extends BaseEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    private String address;

    @OneToMany(mappedBy = "pharmacy")
    private List<MedicineStock> medicineStocks;

    @OneToMany(mappedBy = "pharmacy")
    private List<MedicineOrder> medicineOrders;
}