package com.userservice.domain.mapper;

import com.userservice.domain.dto.RequestClientNonRegisteredDto;
import com.userservice.domain.entity.Client;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@org.mapstruct.Mapper(componentModel = "spring")
public interface MapperToClient {

    @Mappings({
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "passportData.passportNumber", source = "passportNumber"),
            @Mapping(target = "mobilePhone", source = "mobilePhone"),
            @Mapping(target = "countryOfResidence", source = "countryOfResidence")
    })
    Client reguestClientNonRegisteredDtoToClient(RequestClientNonRegisteredDto nonRegisteredDto);
}
