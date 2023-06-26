import com.example.exerciciotestes.controller.request.ClienteRequest;
import com.example.exerciciotestes.model.Cliente;
import com.example.exerciciotestes.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteRepository clienteRepository;

    private List<Cliente> clientes;

    @BeforeEach
    void setup() {
        clientes = Arrays.asList(
                new Cliente(1L, "Cliente 1", 100.0),
                new Cliente(2L, "Cliente 2", 200.0)
        );
    }

    @Test
    void testGetAllCliente_ReturnsClientesList() throws Exception {
        when(clienteRepository.findAll()).thenReturn(clientes);

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(clientes.size()))
                .andExpect(jsonPath("$[0].id").value(clientes.get(0).getId()))
                .andExpect(jsonPath("$[0].nomeCliente").value(clientes.get(0).getNomeCliente()))
                .andExpect(jsonPath("$[0].saldoCliente").value(clientes.get(0).getSaldoCliente()))
                .andExpect(jsonPath("$[1].id").value(clientes.get(1).getId()))
                .andExpect(jsonPath("$[1].nomeCliente").value(clientes.get(1).getNomeCliente()))
                .andExpect(jsonPath("$[1].saldoCliente").value(clientes.get(1).getSaldoCliente()));
    }

    @Test
    void testGetAllCliente_ReturnsEmptyList() throws Exception {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Nenhuma cliente"));
    }

    @Test
    void testGetClienteById_ReturnsCliente() throws Exception {
        Long clienteId = 1L;
        Cliente cliente = new Cliente(clienteId, "Cliente 1", 100.0);
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nomeCliente").value(cliente.getNomeCliente()))
                .andExpect(jsonPath("$.saldoCliente").value(cliente.getSaldoCliente()));
    }

    @Test
    void testGetClienteById_ReturnsNotFound() throws Exception {
        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Nenhum Cliente"));
    }

    @Test
    void testSaveCliente_ReturnsCreated() throws Exception {
        ClienteRequest clienteRequest = new ClienteRequest("Novo Cliente", 300.0);
        Cliente clienteSalvo = new Cliente(3L, clienteRequest.getNomeCliente(), clienteRequest.getSaldoCliente());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clienteSalvo.getId()))
                .andExpect(jsonPath("$.nomeCliente").value(clienteSalvo.getNomeCliente()))
                .andExpect(jsonPath("$.saldoCliente").value(clienteSalvo.getSaldoCliente()));
    }
}
