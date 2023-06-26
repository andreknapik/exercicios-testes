import com.example.exerciciotestes.controller.request.ProdutoRequest;
import com.example.exerciciotestes.model.Produto;
import com.example.exerciciotestes.repository.ProdutoRepository;
import com.example.exerciciotestes.service.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscaTodosProdutos_ReturnsListOfProdutos() {
        // Arrange
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto(1L, "Produto 1", 10.0));
        produtos.add(new Produto(2L, "Produto 2", 20.0));
        Mockito.when(produtoRepository.findAll()).thenReturn(produtos);

        // Act
        List<Produto> produtosRetornados = produtoService.buscaTodosProdutos();

        // Assert
        Assertions.assertEquals(produtos, produtosRetornados);
    }

    @Test
    void testBuscaProdutoPorId_ExistingId_ReturnsProduto() {
        // Arrange
        Long id = 1L;
        Produto produto = new Produto(1L, "Produto 1", 10.0);
        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // Act
        Produto produtoRetornado = produtoService.buscaProdutoPorId(id);

        // Assert
        Assertions.assertEquals(produto, produtoRetornado);
    }

    @Test
    void testBuscaProdutoPorId_NonExistingId_ReturnsNull() {
        // Arrange
        Long id = 1L;
        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Produto produtoRetornado = produtoService.buscaProdutoPorId(id);

        // Assert
        Assertions.assertNull(produtoRetornado);
    }

    @Test
    void testSalvarProduto_ReturnsSavedProduto() {
        // Arrange
        ProdutoRequest produtoRequest = new ProdutoRequest("Produto 1", 10.0);
        Produto produtoMock = new Produto(1L, produtoRequest.getNomeProduto(), produtoRequest.getValorProduto());
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produtoMock);

        // Act
        Produto produtoSalvo = produtoService.salvarProduto(produtoRequest);

        // Assert
        Assertions.assertEquals(produtoMock, produtoSalvo);
    }

    @Test
    void testAtualizarProduto_ExistingId_ReturnsUpdatedProduto() {
        // Arrange
        Long id = 1L;
        ProdutoRequest produtoRequest = new ProdutoRequest("Produto Atualizado", 15.0);
        Produto produtoAtual = new Produto(1L, "Produto 1", 10.0);
        Produto produtoAtualizado = new Produto(1L, produtoRequest.getNomeProduto(), produtoRequest.getValorProduto());

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoAtual));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produtoAtualizado);

        // Act
        Produto produtoRetornado = produtoService.atualizarProduto(id, produtoRequest);

        // Assert
        Assertions.assertEquals(produtoAtualizado, produtoRetornado);
    }

    @Test
    void testAtualizarProduto_NonExistingId_ReturnsNull() {
        // Arrange
        Long id = 1L;
        ProdutoRequest produtoRequest = new ProdutoRequest("Produto Atualizado", 15.0);

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Produto produtoRetornado = produtoService.atualizarProduto(id, produtoRequest);

        // Assert
        Assertions.assertNull(produtoRetornado);
    }

    @Test
    void testDetelaProdutoPorId_ExistingId_DeletesProduto() {
        // Arrange
        Long id = 1L;

        // Act
        produtoService.detelaProdutoPorId(id);

        // Assert
        Mockito.verify(produtoRepository, Mockito.times(1)).deleteById(id);
    }
}
