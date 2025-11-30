package com.ismail.smartShop.service.implementation;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.exception.client.ClientNotFoundException;
import com.ismail.smartShop.mapper.ClientMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.model.enums.Role;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.ClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientResponse createClient(ClientRequest cr) {
        Client client = clientMapper.toEntity(cr);
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        
        Client savedClient = clientRepository.save(client);

        User user = new User();
        user.setUserName(cr.getEmail());
        user.setPassword(passwordEncoder.encode(cr.getPassword()));
        user.setRole(Role.CLIENT);
        user.setClient(savedClient);

        userRepository.save(user);

        savedClient.setUser(user);

        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new ClientNotFoundException());
        return clientMapper.toDto(client);
    }

    @Override
    public ClientResponse updateClientr(Long id, ClientRequest cr) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new ClientNotFoundException());
        client.setEmail(cr.getEmail());
        client.setNom(cr.getNom());
        Client clientSaved = clientRepository.save(client);
        return clientMapper.toDto(clientSaved);
    }

    @Override
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream().map(c -> clientMapper.toDto(c)).toList();
    }

    @Override
    public ClientResponse changeNiveauDeFidelite(Long id , ClientFideliteChangeRequest fid) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new ClientNotFoundException());
        client.setNiveauDeFidelite(NiveauFidelite.valueOf(fid.getNiveauDeFidelite()));
        return clientMapper.toDto(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Client changeFidelite(Client client) {
        if(client.getNiveauDeFidelite().equals(NiveauFidelite.BASIC) && (client.getTotalCommandes() >= 3 || client.getTotalDepense() >=1000) ) {
            client.setNiveauDeFidelite(NiveauFidelite.SILVER);
        }else if (client.getNiveauDeFidelite().equals(NiveauFidelite.SILVER) && (client.getTotalCommandes() >=10 || client.getTotalDepense() >= 5000)) {
            client.setNiveauDeFidelite(NiveauFidelite.GOLD);
        }else if (client.getNiveauDeFidelite().equals(NiveauFidelite.GOLD) && (client.getTotalCommandes() >=20 || client.getTotalDepense() >= 15000)) {
            client.setNiveauDeFidelite(NiveauFidelite.PLATINIUM);
        }
        return client;
    }
    
}
