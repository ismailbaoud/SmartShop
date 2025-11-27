package com.ismail.smartShop.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.exception.client.ClientNotFoundException;
import com.ismail.smartShop.mapper.ClientMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.service.ClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponse createClient(ClientRequest cr) {
        Client client = clientMapper.toEntity(cr);
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);
        return clientMapper.toDto(clientRepository.save(client));
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
    
}
