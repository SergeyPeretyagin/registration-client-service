package com.userservice.domain.entity;

import com.userservice.domain.exception.BadRequestException;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;
import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "country_of_residence")
    private String countryOfResidence;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "passport_data_id", unique = true)
    @ToString.Exclude
    private PassportData passportData;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private UserProfile userProfile;

    @Column(name = "accession_date")
    private Date accessionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_status")
    private ClientStatus clientStatus;

    @Column(name = "mobile_phone", unique = true)
    private String mobilePhone;

    @Column(name = "employer_identification_number")
    private String employerIdentificationNumber;


    public static Predicate<Client> isNotRegistered(){
        return client-> client.getClientStatus()
                .equals(ClientStatus.NOT_REGISTERED);
    }

    public static Predicate<Client> isActive(){
        return client -> client.getClientStatus()
                .equals(ClientStatus.ACTIVE);
    }

    public static Predicate<Client> isNotActive(){
        return client -> client.getClientStatus()
                .equals(ClientStatus.NOT_ACTIVE);
    }

}