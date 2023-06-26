import com.example.exerciciotestes.controller.request.VendaRequest;
import com.example.exerciciotestes.model.Cliente;
import com.example.exerciciotestes.model.Produto;
import com.example.exerciciotestes.model.Venda;
import com.example.exerciciotestes.repository.VendaRepository;
import com.example.exerciciotestes.service.ClienteService;
import com.example.exerciciotestes.service.ProdutoService;
import com.example.exerciciotestes.service.VendaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private VendaService vendaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRealizarVenda_ReturnsVenda() {
        // Arrange
        Long vendaId = 1L;
        Long clienteId = 1L;
        List<Long> produtoIds = Arrays.asList(1L, 2L);
        Cliente cliente = new Cliente(clienteId, "Cliente 1", 100.0);
        List<Produto> produtos = Arrays.asList(
                new Produto(1L, "Produto 1", 10.0),
                new Produto(2L, "Produto 2", 20.0)
        );
        VendaRequest vendaRequest = new VendaRequest(clienteId, produtoIds);

        Mockito.when(clienteService.buscaClientePorId(clienteId)).thenReturn(cliente);
        Mockito.when(produtoService.buscaProdutoPorId(produtoIds)).thenReturn(produtos);

        // Act
        Venda vendaRealizada = vendaService.realizarVenda(vendaRequest);

        // Assert
        Assertions.assertNotNull(vendaRealizada);
        Assertions.assertEquals(vendaId, vendaRealizada.getId());
        Assertions.assertEquals(cliente, vendaRealizada.getCliente());
        Assertions.assertEquals(produtos, vendaRealizada.getProdutos());
    }
}
