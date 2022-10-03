package com.userservice.serviceTest;

import com.userservice.domain.dto.RequestNonClientDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.mapper.TestCreator;
import com.userservice.repository.ClientRepository;
import com.userservice.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp(){
        RequestNonClientDto requestNonClientDto = TestCreator.getRequestNonClientDto();
        client = TestCreator.getNewNotClient(requestNonClientDto);
    }


    @Test
    void testSaveSuccess(){
        when(clientRepository.save(client)).thenReturn(client);
        Client expectedClient = clientService.save(client);
        assertEquals(client,expectedClient);
        verify(clientRepository,times(1)).save(client);
    }

    @Test
    void testSaveNotSuccess(){
        when(clientRepository.save(client)).thenThrow(new InternalServerException("Server is down"));
        assertThrows(InternalServerException.class, ()->when(clientService.save(client)));
    }

    @Test
    void testFindByIdSuccess(){
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        clientService.findById(client.getId());
        verify(clientRepository,times(1)).findById(client.getId());
    }

    @Test
    void testFindByIdNotSuccess(){
        when(clientRepository.findById(client.getId())).thenReturn(Optional.empty());
        Optional<Client> expectedClient = clientService.findById(client.getId());
        assertEquals(Optional.empty(),expectedClient);
        verify(clientRepository,times(1)).findById(client.getId());
    }


    @Test
    void testFindByMobilePhoneSuccess(){
        when(clientRepository.findByMobilePhone(client.getMobilePhone())).thenReturn(Optional.of(client));
        Optional<Client> expectedClient = clientService.findByMobilePhone(client.getMobilePhone());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByMobilePhone(client.getMobilePhone());
    }

    @Test
    void testFindByMobilePhoneNotSuccess(){
        when(clientRepository.findByMobilePhone(client.getMobilePhone())).thenReturn( Optional.empty());
        Optional<Client> expectedClient = clientRepository.findByMobilePhone(client.getMobilePhone());
        assertEquals(Optional.empty(),expectedClient);
        verify(clientRepository,times(1)).findByMobilePhone(client.getMobilePhone());
    }


    @Test
    void testFindByPassportDataPassportNumberSuccess(){
        when(clientRepository.findByPassportData_PassportNumber(client.getPassportData().getPassportNumber())).thenReturn(Optional.of(client));
        Optional<Client> expectedClient = clientService.findByPassportDataPassportNumber(client.getPassportData().getPassportNumber());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByPassportData_PassportNumber(client.getPassportData().getPassportNumber());
    }

    @Test
    void testFindByPassportDataPassportNumberNotSuccess(){
        when(clientRepository.findByPassportData_PassportNumber(client.getPassportData().getPassportNumber())).thenReturn(Optional.empty());
        Optional<Client> expectedClient = clientRepository.findByPassportData_PassportNumber(client.getPassportData().getPassportNumber());
        assertEquals(Optional.empty(),expectedClient);
        verify(clientRepository,times(1)).findByPassportData_PassportNumber(client.getPassportData().getPassportNumber());
    }


    @Test
    void findByPassportDataPassportNumberOrMobilePhoneByMobilePhoneSuccess(){
        when(clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getMobilePhone(),
                client.getMobilePhone())).thenReturn(Optional.of(client));
        Optional<Client> expectedClient = clientService.findByPassportDataPassportNumberOrMobilePhone(client.getMobilePhone());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByPassportData_PassportNumberOrMobilePhone(client.getMobilePhone(),
                client.getMobilePhone());
    }

    @Test
    void findByPassportDataPassportNumberOrMobilePhoneByPassportDataSuccess(){
        when(clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getPassportData().getPassportNumber())).thenReturn(Optional.of(client));
        Optional<Client> expectedClient = clientService.findByPassportDataPassportNumberOrMobilePhone(client.getPassportData().getPassportNumber());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getPassportData().getPassportNumber());
    }

    @Test
    void findByPassportDataPassportNumberOrMobilePhoneNotSuccess(){
        when(clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getPassportData().getPassportNumber())).thenReturn(Optional.empty());
        Optional<Client> expectedClient = clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getPassportData().getPassportNumber());
        assertEquals(Optional.empty(),expectedClient);
        verify(clientRepository,times(1)).findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getPassportData().getPassportNumber());
    }

    @Test
    void testFindByPassportDataPassportNumberOrMobilePhoneSuccess(){
        when(clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getMobilePhone())).thenReturn(Optional.of(client));
        Optional<Client> expectedClient = clientService.findByPassportDataPassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),client.getMobilePhone());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getMobilePhone());
    }
    @Test
    void testFindByPassportDataPassportNumberOrMobilePhoneNotSuccess(){
        when(clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getMobilePhone())).thenReturn(Optional.empty());
        Optional<Client> expectedClient = clientRepository.findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getMobilePhone());
        assertEquals(Optional.empty(),expectedClient);
        verify(clientRepository,times(1)).findByPassportData_PassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),
                client.getMobilePhone());
    }


    @Test
    void testDeleteSuccess(){
        doNothing().when(clientRepository).delete(client);
        clientService.delete(client);
        verify(clientRepository,times(1)).delete(client);
    }
    @Test
    void testDeleteNotSuccess(){
        doThrow(EmptyResultDataAccessException.class).when(clientRepository).delete(client);
        Throwable throwable = catchThrowable(()->{
            clientService.delete(client);
        });
        assertThat(throwable).isInstanceOf(EmptyResultDataAccessException.class);
        verify(clientRepository,times(1)).delete(client);
    }


    @Test
    void testFindByIdOrMobilePhoneSuccess(){
        when(clientRepository.findByIdOrMobilePhone(client.getId(),client.getMobilePhone())).thenReturn(Optional.of(client));
        Optional<Client>expectedClient=clientRepository.findByIdOrMobilePhone(client.getId(),client.getMobilePhone());
        assertEquals(client,expectedClient.get());
        verify(clientRepository,times(1)).findByIdOrMobilePhone(client.getId(),client.getMobilePhone());

    }
    @Test
    void testFindByIdOrMobilePhoneNotSuccess() {
        when(clientRepository.findByIdOrMobilePhone(client.getId(), client.getMobilePhone())).thenReturn(Optional.empty());
        Optional<Client> expectedClient = clientRepository.findByIdOrMobilePhone(client.getId(), client.getMobilePhone());
        assertEquals(Optional.empty(), expectedClient);
        verify(clientRepository, times(1)).findByIdOrMobilePhone(client.getId(), client.getMobilePhone());
    }

    @Test
    void testExistsClientByUUIDReturnTrue(){
        when(clientRepository.existsClientById(client.getId())).thenReturn(true);
        boolean expected = clientRepository.existsClientById(client.getId());
        assertTrue(expected);
        verify(clientRepository,times(1)).existsClientById(client.getId());
    }
    @Test
    void testExistsClientByUUIDReturnFalse(){
        when(clientRepository.existsClientById(client.getId())).thenReturn(false);
        boolean expected = clientRepository.existsClientById(client.getId());
        assertFalse(expected);
        verify(clientRepository,times(1)).existsClientById(client.getId());
    }
}
