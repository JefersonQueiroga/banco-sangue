package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.domain.enums.Sexo;
import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import br.edu.ifrn.bancosangue.exceptions.BusinessException;
import br.edu.ifrn.bancosangue.repositories.DoadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoadorServiceTest {

    @Mock
    private DoadorRepository doadorRepository;

    @InjectMocks
    private DoadorService doadorService;

    private Doador doadorValido;
    private Doador doadorExistente;

    @BeforeEach
    void setUp() {
        doadorValido = new Doador();
        doadorValido.setNomeCompleto("João Silva");
        doadorValido.setCpf("12345678901");
        doadorValido.setSexo(Sexo.MASCULINO);
        doadorValido.setTipoSanguineo(TipoSanguineo.A_POSITIVO);
        doadorValido.setTelefone("11999999999");
        doadorValido.setEmail("joao@email.com");
        doadorValido.setAtivo(true);
        doadorValido.setDataNascimento(LocalDate.of(1990, 5, 15));

        doadorExistente = new Doador();
        doadorExistente.setId(1L);
        doadorExistente.setNomeCompleto("João Silva");
        doadorExistente.setCpf("12345678901");
        doadorExistente.setSexo(Sexo.MASCULINO);
        doadorExistente.setTipoSanguineo(TipoSanguineo.A_POSITIVO);
        doadorExistente.setTelefone("11999999999");
        doadorExistente.setEmail("joao@email.com");
        doadorExistente.setAtivo(true);
        doadorExistente.setDataNascimento(LocalDate.of(1990, 5, 15));
        doadorExistente.setDataCadastro(LocalDateTime.now());
    }

    @Test
    void criarDoador_DeveRetornarDoadorSalvo_QuandoDadosValidos() {
        when(doadorRepository.existsByCpf(anyString())).thenReturn(false);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        Doador resultado = doadorService.criarDoador(doadorValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNomeCompleto()).isEqualTo("João Silva");
        assertThat(resultado.getCpf()).isEqualTo("12345678901");

        verify(doadorRepository, times(1)).existsByCpf("12345678901");
        verify(doadorRepository, times(1)).save(doadorValido);
    }

    @Test
    void criarDoador_DeveLancarBusinessException_QuandoCpfJaExiste() {
        when(doadorRepository.existsByCpf(anyString())).thenReturn(true);

        assertThatThrownBy(() -> doadorService.criarDoador(doadorValido))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CPF já cadastrado.");

        verify(doadorRepository, times(1)).existsByCpf("12345678901");
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void criarDoador_DeveLancarBusinessException_QuandoDataNascimentoNula() {
        doadorValido.setDataNascimento(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.criarDoador(doadorValido));

        assertThat(exception.getMessage()).isEqualTo("Data de nascimento é obrigatória.");

        verify(doadorRepository, never()).existsByCpf(anyString());
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void criarDoador_DeveLancarBusinessException_QuandoIdadeMenorQue16Anos() {
        doadorValido.setDataNascimento(LocalDate.now().minusYears(15));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.criarDoador(doadorValido));

        assertThat(exception.getMessage()).isEqualTo("Idade inválida para doação (permitido entre 16 e 69 anos).");

        verify(doadorRepository, never()).existsByCpf(anyString());
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void criarDoador_DeveLancarBusinessException_QuandoIdadeMaiorQue69Anos() {
        doadorValido.setDataNascimento(LocalDate.now().minusYears(70));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.criarDoador(doadorValido));

        assertThat(exception.getMessage()).isEqualTo("Idade inválida para doação (permitido entre 16 e 69 anos).");

        verify(doadorRepository, never()).existsByCpf(anyString());
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void criarDoador_DeveAceitarDoador_QuandoIdadeE16Anos() {
        doadorValido.setDataNascimento(LocalDate.now().minusYears(16));
        when(doadorRepository.existsByCpf(anyString())).thenReturn(false);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        Doador resultado = doadorService.criarDoador(doadorValido);

        assertThat(resultado).isNotNull();
        verify(doadorRepository, times(1)).save(doadorValido);
    }

    @Test
    void criarDoador_DeveAceitarDoador_QuandoIdadeE69Anos() {
        doadorValido.setDataNascimento(LocalDate.now().minusYears(69));
        when(doadorRepository.existsByCpf(anyString())).thenReturn(false);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        Doador resultado = doadorService.criarDoador(doadorValido);

        assertThat(resultado).isNotNull();
        verify(doadorRepository, times(1)).save(doadorValido);
    }

    @Test
    void listaDoadores_DeveRetornarListaDeDoadores() {
        Doador doador2 = new Doador();
        doador2.setId(2L);
        doador2.setNomeCompleto("Maria Santos");
        doador2.setCpf("98765432100");

        List<Doador> doadores = Arrays.asList(doadorExistente, doador2);
        when(doadorRepository.findAll()).thenReturn(doadores);

        List<Doador> resultado = doadorService.listaDoadores();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNomeCompleto()).isEqualTo("João Silva");
        assertThat(resultado.get(1).getNomeCompleto()).isEqualTo("Maria Santos");

        verify(doadorRepository, times(1)).findAll();
    }

    @Test
    void listaDoadores_DeveRetornarListaVazia_QuandoNaoExistemDoadores() {
        when(doadorRepository.findAll()).thenReturn(Arrays.asList());

        List<Doador> resultado = doadorService.listaDoadores();

        assertThat(resultado).isEmpty();
        verify(doadorRepository, times(1)).findAll();
    }

    @Test
    void obterDoadorPorId_DeveRetornarDoador_QuandoIdExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));

        Doador resultado = doadorService.obterDoadorPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNomeCompleto()).isEqualTo("João Silva");

        verify(doadorRepository, times(1)).findById(1L);
    }

    @Test
    void obterDoadorPorId_DeveLancarBusinessException_QuandoIdNaoExiste() {
        when(doadorRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.obterDoadorPorId(999L));

        assertThat(exception.getMessage()).isEqualTo("Doador não encontrado com o ID: 999");

        verify(doadorRepository, times(1)).findById(999L);
    }

    @Test
    void atualizarDoador_DeveRetornarDoadorAtualizado_QuandoDadosValidos() {
        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setNomeCompleto("João Santos Silva");
        dadosAtualizados.setDataNascimento(LocalDate.of(1985, 10, 20));
        dadosAtualizados.setCpf("12345678901");
        dadosAtualizados.setEmail("joao.santos@email.com");
        dadosAtualizados.setTelefone("11888888888");

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));
        when(doadorRepository.findByCpf("12345678901")).thenReturn(doadorExistente);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        Doador resultado = doadorService.atualizarDoador(1L, dadosAtualizados);

        assertThat(resultado).isNotNull();
        assertThat(doadorExistente.getNomeCompleto()).isEqualTo("João Santos Silva");
        assertThat(doadorExistente.getEmail()).isEqualTo("joao.santos@email.com");
        assertThat(doadorExistente.getTelefone()).isEqualTo("11888888888");

        verify(doadorRepository, times(1)).findById(1L);
        verify(doadorRepository, times(1)).save(doadorExistente);
    }

    @Test
    void atualizarDoador_DeveLancarBusinessException_QuandoIdNaoExiste() {
        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setNomeCompleto("João Santos");

        when(doadorRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.atualizarDoador(999L, dadosAtualizados));

        assertThat(exception.getMessage()).isEqualTo("Doador não encontrado com o ID: 999");

        verify(doadorRepository, times(1)).findById(999L);
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void atualizarDoador_DeveLancarBusinessException_QuandoCpfPertenceAOutroDoador() {
        Doador outroDoador = new Doador();
        outroDoador.setId(2L);
        outroDoador.setCpf("98765432100");

        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setCpf("98765432100");

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));
        when(doadorRepository.findByCpf("98765432100")).thenReturn(outroDoador);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.atualizarDoador(1L, dadosAtualizados));

        assertThat(exception.getMessage()).isEqualTo("CPF já cadastrado para outro doador.");

        verify(doadorRepository, times(1)).findById(1L);
        verify(doadorRepository, times(1)).findByCpf("98765432100");
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void atualizarDoador_DevePermitirAtualizacao_QuandoCpfPertenceAoMesmoDoador() {
        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setCpf("12345678901");
        dadosAtualizados.setEmail("novo@email.com");

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));
        when(doadorRepository.findByCpf("12345678901")).thenReturn(doadorExistente);
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        Doador resultado = doadorService.atualizarDoador(1L, dadosAtualizados);

        assertThat(resultado).isNotNull();
        assertThat(doadorExistente.getEmail()).isEqualTo("novo@email.com");

        verify(doadorRepository, times(1)).findById(1L);
        verify(doadorRepository, times(1)).findByCpf("12345678901");
        verify(doadorRepository, times(1)).save(doadorExistente);
    }

    @Test
    void atualizarDoador_DeveLancarBusinessException_QuandoNovaDataNascimentoInvalida() {
        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setDataNascimento(LocalDate.now().minusYears(15));

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> doadorService.atualizarDoador(1L, dadosAtualizados));

        assertThat(exception.getMessage()).isEqualTo("Idade inválida para doação (permitido entre 16 e 69 anos).");

        verify(doadorRepository, times(1)).findById(1L);
        verify(doadorRepository, never()).save(any(Doador.class));
    }

    @Test
    void atualizarDoador_NaoDeveAtualizarCamposNulos() {
        Doador dadosAtualizados = new Doador();
        dadosAtualizados.setEmail("novo@email.com");
        dadosAtualizados.setTelefone("11777777777");

        String nomeOriginal = doadorExistente.getNomeCompleto();
        LocalDate dataOriginal = doadorExistente.getDataNascimento();
        String cpfOriginal = doadorExistente.getCpf();

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));
        when(doadorRepository.save(any(Doador.class))).thenReturn(doadorExistente);

        doadorService.atualizarDoador(1L, dadosAtualizados);

        assertThat(doadorExistente.getNomeCompleto()).isEqualTo(nomeOriginal);
        assertThat(doadorExistente.getDataNascimento()).isEqualTo(dataOriginal);
        assertThat(doadorExistente.getCpf()).isEqualTo(cpfOriginal);
        assertThat(doadorExistente.getEmail()).isEqualTo("novo@email.com");
        assertThat(doadorExistente.getTelefone()).isEqualTo("11777777777");
    }

    @Test
    void deletarDoador_DeveRemoverDoador_QuandoIdExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorExistente));

        doadorService.deletarDoador(1L);

        verify(doadorRepository, times(1)).findById(1L);
        verify(doadorRepository, times(1)).delete(doadorExistente);
    }


}