package com.userservice.serviceTest;

import com.userservice.domain.dto.RequestNonClientDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.EnumRole;
import com.userservice.domain.entity.Role;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.mapper.TestCreator;
import com.userservice.service.ConnectService;
import com.userservice.service.impl.ClientServiceImpl;
import com.userservice.service.impl.RegistrationServiceImpl;
import com.userservice.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private ClientServiceImpl clientService;
    @Mock
    private RoleServiceImpl roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ConnectService connectService;
    private Client client;

    @BeforeEach
    void setUp(){
        RequestNonClientDto requestNonClientDto = TestCreator.getRequestNonClientDto();
        client = TestCreator.getNewNotClient(requestNonClientDto);
    }

    @Test
    void testRegisterNonClient(){
        when(roleService.findByEnumRole(EnumRole.ROLE_USER)).thenReturn(new Role(UUID.randomUUID(),EnumRole.ROLE_USER));
        Client expectedClient = registrationService.registerNonClient(client);
        assertEquals(client,expectedClient);
        verify(clientService,times(1))
                .findByPassportDataPassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),client.getMobilePhone());
        verify(clientService,times(1)).save(client);
    }

    @Test
    void testRegisterNonClientIfClientExist(){
        when(roleService.findByEnumRole(EnumRole.ROLE_USER)).thenReturn(new Role(UUID.randomUUID(),EnumRole.ROLE_USER));
        Client expectedClient = registrationService.registerNonClient(client);
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),client.getMobilePhone()))
                .thenThrow(new BadRequestException("User is already exists"));
        assertEquals(client,expectedClient);
        assertThrows(BadRequestException.class, ()->when(clientService
                .findByPassportDataPassportNumberOrMobilePhone(client.getPassportData().getPassportNumber(),client.getMobilePhone())));
    }

    @Test
    void testRegisterClientSuccess(){
        List list = new ArrayList();
        when(clientService.findByMobilePhone(anyString())).thenReturn(Optional.of(client));
        when(roleService.findByEnumRole(EnumRole.ROLE_USER)).thenReturn(new Role(UUID.randomUUID(),EnumRole.ROLE_USER));
        when(clientService.save(client)).thenReturn(client);
        when(connectService.getListCardsByClientId(client.getId())).thenReturn(list);
        Client expectedClient = registrationService.registerClient(client);
        assertEquals(client,expectedClient);
        verify(clientService,times(1)).findByMobilePhone(client.getMobilePhone());
        verify(roleService,times(1)).findByEnumRole(EnumRole.ROLE_USER);
        verify(clientService,times(1)).save(client);
    }

    @Test
    void testRegisterClientIfClientNonExist(){
        when(clientService.findByMobilePhone(anyString())).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class, ()->when(clientService.findByMobilePhone(client.getMobilePhone())));
    }

    @ParameterizedTest(name = "#{index} - Run test with client = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#streamClientWithValidPhoneNumberAndStatusIsNotRegistered")
    void testCheckRegistrationWithValidMobilePhoneAndStatusNotRegistered(Optional<Client> validClient){
        lenient().when(clientService.findByMobilePhone(validClient.get().getMobilePhone())).thenReturn(validClient);
        Optional<Client> expectedClient = registrationService.checkRegistration(validClient.get().getMobilePhone());
        assertEquals(validClient,expectedClient);
        verify(clientService,times(1)).findByMobilePhone(validClient.get().getMobilePhone());
    }

    @ParameterizedTest(name = "#{index} - Run test with client = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#streamClientWithNotValidPhoneNumberAndStatusIsNotRegistered")
    void testCheckRegistrationWithNotValidMobilePhoneAndStatusNotRegistered(Optional<Client> validClient){
        assertThrows(BadRequestException.class, ()->when(registrationService.checkRegistration(validClient.get().getMobilePhone())));
    }

    @ParameterizedTest(name = "#{index} - Run test with client = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#streamClientWithValidPhoneNumberAndStatusActiveOrNotActive")
    void testCheckRegistrationWithValidMobilePhoneAndStatusActiveAndNotActive(Optional<Client> validClient){
        lenient().when(clientService.findByMobilePhone(validClient.get().getMobilePhone())).thenReturn(validClient);
        assertThrows(BadRequestException.class, ()->when(registrationService.checkRegistration(validClient.get().getMobilePhone())));
    }

    @Test
    void testRegistrationClientInBankSuccess(){
        when(clientService.findByMobilePhone(client.getMobilePhone())).thenReturn(Optional.empty());
        String expectedString = registrationService.registrationClientInBank(client);
        assertEquals("Operation complete successfully",expectedString);
        verify(clientService,times(1)).findByMobilePhone(client.getMobilePhone());

    }

    @Test
    void testRegistrationClientInBankNotSuccessIfClientExists(){
        when(clientService.findByMobilePhone(client.getMobilePhone())).thenReturn(Optional.of(client));
        assertThrows(BadRequestException.class, ()->when(registrationService.registrationClientInBank(client)));
        verify(clientService,times(1)).findByMobilePhone(client.getMobilePhone());
    }
}
