package com.userservice.repository;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.PassportData;
import com.userservice.domain.entity.UserProfile;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface
ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByMobilePhone(String mobilePhone);

    Optional<Client> findByPassportData_PassportNumber(String passportNumber);

    Optional<Client> findByPassportData_PassportNumberOrMobilePhone(String passportNumber, String mobilePhone);

    Optional<Client> findByUserProfile_Email(String email);

    Optional<Client> findByIdOrMobilePhone(UUID uuid, String mobilePhone);

    @Modifying
    @Query("delete from Client c where c.id = ?1")
    void deleteClientById(UUID id);

    @Modifying
    @Query("delete from Client c where c.id = ?1")
    void deleteById(UUID id);

    void deleteClientByPassportData(PassportData passportData);

    void deleteClientByUserProfile(UserProfile userProfile);
    boolean existsClientById(UUID id);
}