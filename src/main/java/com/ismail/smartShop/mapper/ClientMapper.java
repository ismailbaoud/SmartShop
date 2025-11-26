package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.client.request.ClientRequest;
import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.model.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientRequest clientRequest);
    ClientResponse toDto(Client client);
}
