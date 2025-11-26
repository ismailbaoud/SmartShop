package com.ismail.smartShop.model;

import java.util.List;

import com.ismail.smartShop.model.enums.NiveauFidelite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    NiveauFidelite niveauDeFidelite;

    private Integer totalCommandes = 0;
    private Double TotalDepense = 0.0;

    @OneToOne(mappedBy = "client")
    User user;

    @OneToMany(mappedBy = "client")
    private List<Order> orders;
}