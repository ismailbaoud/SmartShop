package com.ismail.smartShop.service;

import java.util.List;

import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;

public interface ClientService {

    ClientResponse createClient(ClientRequest cr);


    ClientResponse getClientById(Long id);

    ClientResponse updateClientr(Long id,ClientRequest cr);

    List<ClientResponse> getAllClients();

    ClientResponse changeNiveauDeFidelite(Long id , ClientFideliteChangeRequest fid);

    void deleteClient(Long id);
}