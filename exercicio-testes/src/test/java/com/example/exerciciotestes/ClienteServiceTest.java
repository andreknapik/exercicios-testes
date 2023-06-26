package com.example.exerciciotestes.service;

import com.example.exerciciotestes.controller.request.ClienteRequest;
import com.example.exerciciotestes.model.Cliente;
import com.example.exerciciotestes.repository.ClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscaTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1L, "Cliente 1", 100.0));
        clientes.add(new Cliente(2L, "Cliente 2", 200.0));
        Mockito.when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.buscaTodosClientes();
        Assertions.assertEquals(clientes.size(), result.size());
        Assertions.assertEquals(clientes.get(0), result.get(0));
        Assertions.assertEquals(clientes.get(1), result.get(1));
    }

    @Test
    void testBuscaClientePorId() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente(clienteId, "Cliente 1", 100.0);
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscaClientePorId(clienteId);

        Assertions.assertEquals(cliente, result);
    }

    @Test
    void testBuscaClientePorId_NotFound() {
        Long clienteId = 1L;
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        Cliente result = clienteService.buscaClientePorId(clienteId);

        Assertions.assertNull(result);
    }

    @Test
    void testSalvarCliente() {
        // Arrange
        ClienteRequest clienteRequest = new ClienteRequest("Novo Cliente", 500.0);
        Cliente clienteSalvo = new Cliente(1L, "Novo Cliente", 500.0);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        Cliente result = clienteService.salvarCliente(clienteRequest);

        // Assert
        Assertions.assertEquals(clienteSalvo, result);
    }

    @Test
    void testAtualizarCliente_ExistingCliente_ReturnsUpdatedCliente() {
        // Arrange
        Long clienteId = 1L;
        ClienteRequest clienteRequest = new ClienteRequest("Cliente Atualizado", 1000.0);
        Cliente clienteExistente = new Cliente(clienteId, "Cliente Antigo", 500.0);
        Cliente clienteAtualizado = new Cliente(clienteId, "Cliente Atualizado", 1000.0);
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.ofNullable(clienteExistente));
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteAtualizado);

        // Act
        Cliente result = clienteService.atualizarCliente(clienteId, clienteRequest);

        // Assert
        Assertions.assertEquals(clienteAtualizado, result);
    }

    @Test
    void testAtualizarCliente_NonExistingCliente_ReturnsNull() {
        // Arrange
        Long clienteId = 1L;
        ClienteRequest clienteRequest = new ClienteRequest("Cliente Atualizado", 1000.0);
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.empty());

        // Act
        Cliente result = clienteService.atualizarCliente(clienteId, clienteRequest);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    void testDetelaClientePorId_ExistingCliente_DeletesCliente() {
        // Arrange
        Long clienteId = 1L;
        Cliente clienteExistente = new Cliente(clienteId, "Cliente Existente", 1000.0);
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.ofNullable(clienteExistente));

        // Act
        clienteService.detelaClientePorId(clienteId);

        // Assert
        Mockito.verify(clienteRepository, Mockito.times(1)).deleteById(clienteId);
    }

    @Test
    void testDetelaClientePorId_NonExistingCliente_ThrowsException() {
        // Arrange
        Long clienteId = 1L;
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            clienteService.detelaClientePorId(clienteId);
        });
    }


}