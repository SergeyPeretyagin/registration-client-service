package com.userservice.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "sms_notification")
    private Boolean smsNotification;

    @Column(name = "push_notification")
    private Boolean pushNotification;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "security_question")
    private String securityQuestion;

    @Column(name = "security_answer")
    private String securityAnswer;

    @Column(name = "app_registration_date")
    private Date appRegistrationDate;

    @Column(name = "email_subscription")
    private Boolean emailSubscription;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    @ManyToMany
    @JoinTable(name = "user_profile_roles",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();
}