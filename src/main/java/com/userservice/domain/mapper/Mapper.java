package com.userservice.domain.mapper;

import com.userservice.domain.dto.ClientInformationDto;
import com.userservice.domain.dto.NotificationDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    @Mappings({
            @Mapping(target = "countryOfResidence", source = "client.countryOfResidence"),
            @Mapping(target = "email", source = "client.userProfile.email",defaultValue = "null"),
            @Mapping(target = "firstName", source = "client.firstName"),
            @Mapping(target = "lastName", source = "client.lastName"),
            @Mapping(target = "middleName", source = "client.middleName",defaultValue = "null"),
            @Mapping(target = "passportNumber", source = "client.passportData.passportNumber")})
    ClientInformationDto clientToInformationDto(Client client);

    @Mappings({
            @Mapping(target = "email",source = "userProfile.email", defaultValue = "null"),
            @Mapping(target = "smsNotification",source = "userProfile.smsNotification"),
            @Mapping(target = "pushNotification",source = "userProfile.pushNotification"),
            @Mapping(target = "emailSubscription",source = "userProfile.emailSubscription")
    })
    NotificationDto userProfileToNotificationDto(UserProfile userProfile);

}
