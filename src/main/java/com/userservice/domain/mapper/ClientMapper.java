package com.userservice.domain.mapper;

import com.userservice.domain.dto.*;
import com.userservice.domain.entity.Client;

import java.util.Optional;

public interface ClientMapper {
    ClientDto clientToClientDto(Client client);

    Client registerNonClientDtoToClient(RequestNonClientDto requestNonClientDto);

    ResponseNonClientDto clientToResponseNonClientDto(Client client);

    ResponseClientDto clientToResponseClientDto(Client client);

    Client requestClientDtoToClient(RequestClientDto requestClientDto);

}
