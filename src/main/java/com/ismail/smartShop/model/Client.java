package com.ismail.smartShop.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ismail.smartShop.model.enums.NiveauFidelite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nom;
    String email;

    @Enumerated(EnumType.STRING)
    private NiveauFidelite niveauDeFidelite;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalCommandes = 0;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private Double totalDepense = 0.0;

    @OneToOne(mappedBy = "client")
    User user;

    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}
