package com.userservice.serviceTest;

import com.userservice.domain.entity.Client;
import com.userservice.domain.exception.NotExistsUserException;
import com.userservice.repository.ClientRepository;
import com.userservice.service.impl.DeleteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DeleteServiceTest {
    @InjectMocks
    private DeleteServiceImpl deleteService;
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Client client;

    @Test
    void testDeleteUserFromClientDBSuccess(){
        UUID userId = UUID.randomUUID();
        when(clientRepository.findById(userId)).thenReturn(Optional.of(client));
        String expected = deleteService.deleteUserByUUID(userId);
        assertEquals("User account deleted successfully",expected);
        verify(clientRepository,times(1)).findById(userId);
        verify(clientRepository,times(1)).deleteClientByPassportData(client.getPassportData());
    }

    @Test
    void testDeleteUserFromClientDBNotSuccessIfClientIsNotExist(){
        UUID userId = UUID.randomUUID();
        when(clientRepository.findById(userId)).thenThrow(new NotExistsUserException("This user does not exist"));
        assertThrows(NotExistsUserException.class,()->deleteService.deleteUserByUUID(userId));
        verify(clientRepository,times(1)).findById(userId);
    }
    @Test
    void testDeleteUserFromDBByPhoneNumberSuccess(){
        when(clientRepository.findByMobilePhone(anyString())).thenReturn(Optional.of(client));
        String expected = deleteService.deleteUserByPhoneNumber(anyString());
        assertEquals("User account deleted successfully",expected);
        verify(clientRepository,times(1)).findByMobilePhone(anyString());
        verify(clientRepository,times(1)).deleteClientByPassportData(client.getPassportData());
    }

    @Test
    void testDeleteUserFromDBByPhoneNumberIfPhoneNumberNotExist(){
        when(clientRepository.findByMobilePhone(anyString())).thenThrow(new NotExistsUserException("This user does not exist"));
        assertThrows(NotExistsUserException.class,()->deleteService.deleteUserByPhoneNumber(anyString()));
        verify(clientRepository,times(1)).findByMobilePhone(anyString());
    }


}
