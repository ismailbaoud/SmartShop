package com.ismail.smartShop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.dto.client.request.ClientFideliteChangeRequest;
import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.service.implementation.ClientServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientServiceImpl clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest clientRequest) {
        ClientResponse client =  clientService.createClient(clientRequest);
        return ResponseEntity.ok().body(client);
    }


    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getMethodName(@PathVariable Long id) {
        ClientResponse client = clientService.getClientById(id);
        return ResponseEntity.ok().body(client);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@Valid @PathVariable Long id, @RequestBody ClientRequest clientRequest) {        
        ClientResponse clientResponse = clientService.updateClientr(id, clientRequest);
        return ResponseEntity.ok().body(clientResponse);
    }

    @PutMapping("/{id}/fidelite")
    public ResponseEntity<ClientResponse> putMethodName(@Valid @PathVariable Long id, @RequestBody ClientFideliteChangeRequest fidelite) {
        ClientResponse clientResponse = clientService.changeNiveauDeFidelite(id, fidelite);
        return ResponseEntity.ok().body(clientResponse);
    }
}
