package com.userservice.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "verification")
public class Verification {
    @Id
    @Column(name = "receiver")
    private String receiver;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "code_expiration")
    private Timestamp codeExpiration;

    @Column(name = "block_expiration")
    private Timestamp blockExpiration;

    @Column(name = "count_request")
    private int countRequest;

    @Column(name = "type")
    private String type;

}