package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.exception.client.ClientNotFoundException;
import com.ismail.smartShop.helper.passwordHasher;
import com.ismail.smartShop.mapper.ClientMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.model.enums.Role;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.implementation.ClientServiceImpl;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private passwordHasher passwordEncoder;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequest clientRequest;
    private ClientResponse clientResponse;
    private User user;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setNom("John Doe");
        client.setEmail("john@test.com");
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        client.setTotalCommandes(0);
        client.setTotalDepense(0.0);

        clientRequest = ClientRequest.builder()
            .nom("John Doe")
            .email("john@test.com")
            .password("password123")
            .build();

        clientResponse = new ClientResponse(
            1L,
            "John Doe",
            "john@test.com",
            0,
            0.0,
            NiveauFidelite.BASIC,
            null,
            null
        );

        user = new User();
        user.setId(1L);
        user.setUserName("john@test.com");
        user.setRole(Role.CLIENT);
    }

    @Test
    void createClient_ShouldCreateClientAndUser_WhenValidRequest() {
        // Arrange
        when(clientMapper.toEntity(clientRequest)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(passwordEncoder.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(clientMapper.toDto(client)).thenReturn(clientResponse);

        // Act
        ClientResponse result = clientService.createClient(clientRequest);

        // Assert
        assertNotNull(result);
        assertEquals(clientResponse.id(), result.id());
        assertEquals(NiveauFidelite.BASIC, client.getNiveauDeFidelite());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).hash("password123");
    }

    @Test
    void getClientById_ShouldReturnClient_WhenClientExists() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientResponse);

        // Act
        ClientResponse result = clientService.getClientById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void getClientById_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(999L));
        verify(clientRepository, times(1)).findById(999L);
    }

    @Test
    void updateClient_ShouldUpdateClient_WhenClientExists() {
        // Arrange
        ClientRequest updateRequest = ClientRequest.builder()
            .nom("Jane Doe")
            .email("jane@test.com")
            .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientResponse);

        // Act
        ClientResponse result = clientService.updateClientr(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", client.getNom());
        assertEquals("jane@test.com", client.getEmail());
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void updateClient_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.updateClientr(999L, clientRequest));
    }

    @Test
    void getAllClients_ShouldReturnListOfClients() {
        // Arrange
        Client client2 = new Client();
        client2.setId(2L);
        List<Client> clients = Arrays.asList(client, client2);
        
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toDto(any(Client.class))).thenReturn(clientResponse);

        // Act
        List<ClientResponse> result = clientService.getAllClients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void changeNiveauDeFidelite_ShouldUpdateFidelite_WhenClientExists() {
        // Arrange
        ClientFideliteChangeRequest fideliteRequest = new ClientFideliteChangeRequest();
        fideliteRequest.setNiveauDeFidelite("GOLD");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientResponse);

        // Act
        ClientResponse result = clientService.changeNiveauDeFidelite(1L, fideliteRequest);

        // Assert
        assertNotNull(result);
        assertEquals(NiveauFidelite.GOLD, client.getNiveauDeFidelite());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void changeNiveauDeFidelite_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        ClientFideliteChangeRequest fideliteRequest = new ClientFideliteChangeRequest();
        fideliteRequest.setNiveauDeFidelite("GOLD");
        
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.changeNiveauDeFidelite(999L, fideliteRequest));
    }

    @Test
    void deleteClient_ShouldDeleteClient() {
        // Arrange
        doNothing().when(clientRepository).deleteById(1L);

        // Act
        clientService.deleteClient(1L);

        // Assert
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void changeFidelite_ShouldUpgradeToSilver_WhenBasicWith3Orders() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        client.setTotalCommandes(3);
        client.setTotalDepense(500.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.SILVER, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldUpgradeToSilver_WhenBasicWith1000Spent() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        client.setTotalCommandes(1);
        client.setTotalDepense(1000.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.SILVER, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldUpgradeToGold_WhenSilverWith10Orders() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.SILVER);
        client.setTotalCommandes(10);
        client.setTotalDepense(3000.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.GOLD, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldUpgradeToGold_WhenSilverWith5000Spent() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.SILVER);
        client.setTotalCommandes(5);
        client.setTotalDepense(5000.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.GOLD, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldUpgradeToPlatinium_WhenGoldWith20Orders() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.GOLD);
        client.setTotalCommandes(20);
        client.setTotalDepense(10000.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.PLATINIUM, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldUpgradeToPlatinium_WhenGoldWith15000Spent() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.GOLD);
        client.setTotalCommandes(10);
        client.setTotalDepense(15000.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.PLATINIUM, result.getNiveauDeFidelite());
    }

    @Test
    void changeFidelite_ShouldNotUpgrade_WhenConditionsNotMet() {
        // Arrange
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        client.setTotalCommandes(2);
        client.setTotalDepense(500.0);

        // Act
        Client result = clientService.changeFidelite(client);

        // Assert
        assertEquals(NiveauFidelite.BASIC, result.getNiveauDeFidelite());
    }

    @Test
    void getProfile_ShouldReturnClientProfile_WhenUserInSession() {
        // Arrange
        when(session.getAttribute("USER")).thenReturn(user);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientResponse);

        // Act
        ClientResponse result = clientService.getProfile(session);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(session, times(1)).getAttribute("USER");
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void getProfile_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        when(session.getAttribute("USER")).thenReturn(user);
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.getProfile(session));
    }
}
