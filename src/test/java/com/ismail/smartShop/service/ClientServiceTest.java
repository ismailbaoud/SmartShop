// package com.ismail.smartShop.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.*;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.ismail.smartShop.dto.client.request.ClientRequest;
// import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
// import com.ismail.smartShop.dto.client.response.ClientResponse;
// import com.ismail.smartShop.mapper.ClientMapper;
// import com.ismail.smartShop.model.Client;
// import com.ismail.smartShop.model.enums.NiveauFidelite;
// import com.ismail.smartShop.repository.ClientRepository;
// import com.ismail.smartShop.service.implementation.ClientServiceImpl;

// @ExtendWith(MockitoExtension.class)
// class ClientServiceTest {

//     @Mock
//     private ClientRepository clientRepository;

//     @Mock
//     private ClientMapper clientMapper;

//     @InjectMocks
//     private ClientServiceImpl clientService;

//     private Client client;
//     private ClientRequest clientRequest;
//     private ClientResponse clientResponse;

//     @BeforeEach
//     void setUp() {

//         client = Client.builder()
//                 .id(1L)
//                 .nom("Ismail")
//                 .email("email@gmail.com")
//                 .niveauDeFidelite(NiveauFidelite.BASIC)
//                 .build();

//         clientRequest = ClientRequest.builder()
//                 .nom("Ismail")
//                 .email("email@gmail.com")
//                 .build();

//         clientResponse = new ClientResponse(
//                 1L,
//                 "Ismail",
//                 "email@gmail.com",
//                 0,
//                 0.0,
//                 NiveauFidelite.BASIC,
//                 null,
//                 null
//         );
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should create a new client")
//     void itShouldReturnCreatedClient() {

//         when(clientMapper.toEntity(clientRequest)).thenReturn(client);
//         when(clientRepository.save(any(Client.class))).thenReturn(client);
//         when(clientMapper.toDto(client)).thenReturn(clientResponse);

//         ClientResponse result = clientService.createClient(clientRequest);

//         assertNotNull(result);
//         assertEquals(clientRequest.getEmail(), result.email());
//         assertEquals(clientRequest.getNom(), result.nom());

//         verify(clientMapper).toEntity(clientRequest);
//         verify(clientRepository).save(client);
//         verify(clientMapper).toDto(client);
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should return client by ID")
//     void itShouldReturnClientById() {

//         when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//         when(clientMapper.toDto(client)).thenReturn(clientResponse);

//         ClientResponse result = clientService.getClientById(1L);

//         assertNotNull(result);
//         assertEquals(1L, result.id());

//         verify(clientRepository).findById(1L);
//         verify(clientMapper).toDto(client);
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should update client successfully")
//     void itShouldUpdateClient() {

//         when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//         when(clientRepository.save(any(Client.class))).thenReturn(client);
//         when(clientMapper.toDto(client)).thenReturn(clientResponse);

//         ClientResponse result = clientService.updateClientr(1L, clientRequest);

//         assertNotNull(result);
//         assertEquals(clientRequest.getNom(), result.nom());

//         verify(clientRepository).findById(1L);
//         verify(clientRepository).save(client);
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should return all clients")
//     void itShouldReturnAllClients() {

//         when(clientRepository.findAll()).thenReturn(List.of(client));
//         when(clientMapper.toDto(client)).thenReturn(clientResponse);

//         List<ClientResponse> result = clientService.getAllClients();

//         assertEquals(1, result.size());
//         assertEquals(client.getEmail(), result.get(0).email());

//         verify(clientRepository).findAll();
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should change client's loyalty level")
//     void itShouldChangeNiveauDeFidelite() {

//         ClientFideliteChangeRequest req = new ClientFideliteChangeRequest("PLATINIUM");

//         // Mock repository and mapper
//         when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//         when(clientRepository.save(any(Client.class))).thenReturn(client);

//         // Update the expected response loyalty level
//         ClientResponse updatedResponse = new ClientResponse(
//                 1L,
//                 "Ismail",
//                 "email@gmail.com",
//                 0,
//                 0.0,
//                 NiveauFidelite.PLATINIUM,
//                 null,
//                 null
//         );

//         when(clientMapper.toDto(client)).thenReturn(updatedResponse);

//         // Call service
//         ClientResponse result = clientService.changeNiveauDeFidelite(1L, req);

//         assertNotNull(result);
//         assertEquals(NiveauFidelite.PLATINIUM, result.niveauFidelite());

//         verify(clientRepository).findById(1L);
//         verify(clientRepository).save(client);
//     }

//     // ----------------------------------------------------------------------------------

//     @Test
//     @DisplayName("Should delete a client by ID")
//     void itShouldDeleteClient() {

//         doNothing().when(clientRepository).deleteById(anyLong());

//         clientService.deleteClient(1L);

//         verify(clientRepository).deleteById(1L);
//     }
// }
