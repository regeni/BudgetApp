package com.budgetapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "all_budget_records")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class BudgetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Ide majd ki kell találni, hogy mi lenne a megfelelő, sok más helyen IDENTITY van
//    @Column(name = "id")
    private Integer id;

    @Column(name = "tranzakcio_datuma")
    private String tranzakcioDatuma;

    @Column(name = "bejovo_kimeno")
    private String bejovoKimeno;

    @Column(name = "partner_neve")
    private String partnerNeve;

    @Column(name = "koltesi_kategoria")
    private String koltesiKategoria;

    @Column(name = "szamla_nev")
    private String szamlaNev;

    @Column(name = "osszeg")
    private Integer osszeg;

    @Column(name = "megjegyzes")
    private String megjegyzes;

}
