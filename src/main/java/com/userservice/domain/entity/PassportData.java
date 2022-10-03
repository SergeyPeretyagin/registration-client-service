package com.userservice.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "passport_data")
public class PassportData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "passport_number", unique = true)
    private String passportNumber;

    @Column(name = "issuance_date")
    private Date issuanceDate;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "birth_date")
    private Date birthDate;

    @OneToOne(mappedBy = "passportData", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Client client;

}