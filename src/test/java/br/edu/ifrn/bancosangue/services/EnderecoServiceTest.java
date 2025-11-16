package br.edu.ifrn.bancosangue.services;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.domain.entities.EnderecoDoador;
import br.edu.ifrn.bancosangue.domain.enums.Sexo;
import br.edu.ifrn.bancosangue.domain.enums.TipoSanguineo;
import br.edu.ifrn.bancosangue.dtos.EnderecoDoadorDTO;
import br.edu.ifrn.bancosangue.exceptions.BusinessException;
import br.edu.ifrn.bancosangue.mappers.EnderecoDoadorMapper;
import br.edu.ifrn.bancosangue.repositories.DoadorRepository;
import br.edu.ifrn.bancosangue.repositories.EnderecoRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private DoadorRepository doadorRepository;

    @Mock
    private EnderecoDoadorMapper enderecoMapper;

    @InjectMocks
    private EnderecoService enderecoService;

    private Doador doadorValido;
    private EnderecoDoadorDTO enderecoDTO;
    private EnderecoDoador enderecoEntity;
    private EnderecoDoador enderecoSalvo;

    @BeforeEach
    void setUp() {
        doadorValido = new Doador();
        doadorValido.setId(1L);
        doadorValido.setNomeCompleto("João Silva");
        doadorValido.setCpf("12345678901");
        doadorValido.setSexo(Sexo.MASCULINO);
        doadorValido.setTipoSanguineo(TipoSanguineo.A_POSITIVO);
        doadorValido.setAtivo(true);
        doadorValido.setDataNascimento(LocalDate.of(1990, 5, 15));

        enderecoDTO = new EnderecoDoadorDTO();
        enderecoDTO.setLogradouro("Rua das Flores");
        enderecoDTO.setNumero("123");
        enderecoDTO.setBairro("Centro");
        enderecoDTO.setCidade("São Paulo");
        enderecoDTO.setEstado("SP");
        enderecoDTO.setCep("01000000");

        enderecoEntity = new EnderecoDoador();
        enderecoEntity.setLogradouro("Rua das Flores");
        enderecoEntity.setNumero("123");
        enderecoEntity.setBairro("Centro");
        enderecoEntity.setCidade("São Paulo");
        enderecoEntity.setEstado("SP");
        enderecoEntity.setCep("01000000");

        enderecoSalvo = new EnderecoDoador();
        enderecoSalvo.setId(1L);
        enderecoSalvo.setDoador(doadorValido);
        enderecoSalvo.setLogradouro("Rua das Flores");
        enderecoSalvo.setNumero("123");
        enderecoSalvo.setBairro("Centro");
        enderecoSalvo.setCidade("São Paulo");
        enderecoSalvo.setEstado("SP");
        enderecoSalvo.setCep("01000000");
        enderecoSalvo.setCriadoEm(LocalDateTime.now());
        enderecoSalvo.setAtualizadoEm(LocalDateTime.now());
    }

    @Test
    void criarEndereco_DeveRetornarEnderecoDTO_QuandoDadosValidos() {
        EnderecoDoadorDTO enderecoRetornadoDTO = new EnderecoDoadorDTO();
        enderecoRetornadoDTO.setId(1L);
        enderecoRetornadoDTO.setLogradouro("Rua das Flores");
        enderecoRetornadoDTO.setNumero("123");
        enderecoRetornadoDTO.setBairro("Centro");
        enderecoRetornadoDTO.setCidade("São Paulo");
        enderecoRetornadoDTO.setEstado("SP");
        enderecoRetornadoDTO.setCep("01000000");

        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorValido));
        when(enderecoMapper.toEntity(enderecoDTO)).thenReturn(enderecoEntity);
        when(enderecoRepository.save(any(EnderecoDoador.class))).thenReturn(enderecoSalvo);
        when(enderecoMapper.toDTO(enderecoSalvo)).thenReturn(enderecoRetornadoDTO);

        EnderecoDoadorDTO resultado = enderecoService.criarEndereco(1L, enderecoDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getLogradouro()).isEqualTo("Rua das Flores");
        assertThat(resultado.getNumero()).isEqualTo("123");
        assertThat(resultado.getBairro()).isEqualTo("Centro");
        assertThat(resultado.getCidade()).isEqualTo("São Paulo");
        assertThat(resultado.getEstado()).isEqualTo("SP");
        assertThat(resultado.getCep()).isEqualTo("01000000");

        verify(doadorRepository, times(1)).findById(1L);
        verify(enderecoMapper, times(1)).toEntity(enderecoDTO);
        verify(enderecoRepository, times(1)).save(any(EnderecoDoador.class));
        verify(enderecoMapper, times(1)).toDTO(enderecoSalvo);
    }

    @Test
    void criarEndereco_DeveLancarBusinessException_QuandoDoadorNaoExiste() {
        when(doadorRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.criarEndereco(999L, enderecoDTO));

        assertThat(exception.getMessage()).isEqualTo("Doador não encontrado com ID: 999");

        verify(doadorRepository, times(1)).findById(999L);
        verify(enderecoMapper, never()).toEntity(any());
        verify(enderecoRepository, never()).save(any());
        verify(enderecoMapper, never()).toDTO(any());
    }

    @Test
    void criarEndereco_DeveDefinirDoadorETimestamps() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doadorValido));
        when(enderecoMapper.toEntity(enderecoDTO)).thenReturn(enderecoEntity);
        when(enderecoRepository.save(any(EnderecoDoador.class))).thenReturn(enderecoSalvo);
        when(enderecoMapper.toDTO(enderecoSalvo)).thenReturn(new EnderecoDoadorDTO());

        enderecoService.criarEndereco(1L, enderecoDTO);

        verify(enderecoRepository).save(argThat(endereco -> {
            assertThat(endereco.getDoador()).isEqualTo(doadorValido);
            assertThat(endereco.getCriadoEm()).isNotNull();
            assertThat(endereco.getAtualizadoEm()).isNotNull();
            return true;
        }));
    }

    @Test
    void atualizarEndereco_DeveRetornarEnderecoAtualizado_QuandoDadosValidos() {
        EnderecoDoadorDTO enderecoAtualizadoDTO = new EnderecoDoadorDTO();
        enderecoAtualizadoDTO.setLogradouro("Rua Nova");
        enderecoAtualizadoDTO.setNumero("456");

        EnderecoDoadorDTO enderecoRetornadoDTO = new EnderecoDoadorDTO();
        enderecoRetornadoDTO.setId(1L);
        enderecoRetornadoDTO.setLogradouro("Rua Nova");
        enderecoRetornadoDTO.setNumero("456");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoSalvo));
        when(enderecoRepository.save(any(EnderecoDoador.class))).thenReturn(enderecoSalvo);
        when(enderecoMapper.toDTO(enderecoSalvo)).thenReturn(enderecoRetornadoDTO);

        EnderecoDoadorDTO resultado = enderecoService.atualizarEndereco(1L, 1L, enderecoAtualizadoDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getLogradouro()).isEqualTo("Rua Nova");
        assertThat(resultado.getNumero()).isEqualTo("456");

        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoMapper, times(1)).atualizarFromDTO(enderecoAtualizadoDTO, enderecoSalvo);
        verify(enderecoRepository, times(1)).save(enderecoSalvo);
        verify(enderecoMapper, times(1)).toDTO(enderecoSalvo);
    }

    @Test
    void atualizarEndereco_DeveLancarBusinessException_QuandoEnderecoNaoExiste() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.atualizarEndereco(1L, 999L, enderecoDTO));

        assertThat(exception.getMessage()).isEqualTo("Endereço não encontrado com ID: 999");

        verify(enderecoRepository, times(1)).findById(999L);
        verify(enderecoMapper, never()).atualizarFromDTO(any(), any());
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    void atualizarEndereco_DeveLancarBusinessException_QuandoEnderecoNaoPertenceAoDoador() {
        Doador outroDoador = new Doador();
        outroDoador.setId(2L);

        EnderecoDoador enderecoDeOutroDoador = new EnderecoDoador();
        enderecoDeOutroDoador.setId(1L);
        enderecoDeOutroDoador.setDoador(outroDoador);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoDeOutroDoador));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.atualizarEndereco(1L, 1L, enderecoDTO));

        assertThat(exception.getMessage()).isEqualTo("Endereço não pertence ao doador informado");

        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoMapper, never()).atualizarFromDTO(any(), any());
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    void atualizarEndereco_DeveAtualizarTimestamp() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoSalvo));
        when(enderecoRepository.save(any(EnderecoDoador.class))).thenReturn(enderecoSalvo);
        when(enderecoMapper.toDTO(enderecoSalvo)).thenReturn(new EnderecoDoadorDTO());

        enderecoService.atualizarEndereco(1L, 1L, enderecoDTO);

        verify(enderecoRepository).save(argThat(endereco -> {
            assertThat(endereco.getAtualizadoEm()).isNotNull();
            return true;
        }));
    }

    @Test
    void removerEndereco_DeveRemoverEndereco_QuandoDadosValidos() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoSalvo));

        enderecoService.removerEndereco(1L, 1L);

        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoRepository, times(1)).delete(enderecoSalvo);
    }

    @Test
    void removerEndereco_DeveLancarBusinessException_QuandoEnderecoNaoExiste() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.removerEndereco(1L, 999L));

        assertThat(exception.getMessage()).isEqualTo("Endereço não encontrado com ID: 999");

        verify(enderecoRepository, times(1)).findById(999L);
        verify(enderecoRepository, never()).delete(any());
    }

    @Test
    void removerEndereco_DeveLancarBusinessException_QuandoEnderecoNaoPertenceAoDoador() {
        Doador outroDoador = new Doador();
        outroDoador.setId(2L);

        EnderecoDoador enderecoDeOutroDoador = new EnderecoDoador();
        enderecoDeOutroDoador.setId(1L);
        enderecoDeOutroDoador.setDoador(outroDoador);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoDeOutroDoador));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.removerEndereco(1L, 1L));

        assertThat(exception.getMessage()).isEqualTo("Endereço não pertence ao doador informado");

        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoRepository, never()).delete(any());
    }

    @Test
    void listarEnderecosPorDoador_DeveRetornarListaDeEnderecos_QuandoDoadorExiste() {
        EnderecoDoador endereco2 = new EnderecoDoador();
        endereco2.setId(2L);
        endereco2.setDoador(doadorValido);
        endereco2.setLogradouro("Avenida Principal");
        endereco2.setCidade("São Paulo");

        List<EnderecoDoador> enderecos = Arrays.asList(enderecoSalvo, endereco2);

        EnderecoDoadorDTO enderecoDTO1 = new EnderecoDoadorDTO();
        enderecoDTO1.setId(1L);
        enderecoDTO1.setLogradouro("Rua das Flores");

        EnderecoDoadorDTO enderecoDTO2 = new EnderecoDoadorDTO();
        enderecoDTO2.setId(2L);
        enderecoDTO2.setLogradouro("Avenida Principal");

        List<EnderecoDoadorDTO> enderecosDTO = Arrays.asList(enderecoDTO1, enderecoDTO2);

        when(doadorRepository.existsById(1L)).thenReturn(true);
        when(enderecoRepository.findByDoadorId(1L)).thenReturn(enderecos);
        when(enderecoMapper.toDTOList(enderecos)).thenReturn(enderecosDTO);

        List<EnderecoDoadorDTO> resultado = enderecoService.listarEnderecosPorDoador(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(0).getLogradouro()).isEqualTo("Rua das Flores");
        assertThat(resultado.get(1).getId()).isEqualTo(2L);
        assertThat(resultado.get(1).getLogradouro()).isEqualTo("Avenida Principal");

        verify(doadorRepository, times(1)).existsById(1L);
        verify(enderecoRepository, times(1)).findByDoadorId(1L);
        verify(enderecoMapper, times(1)).toDTOList(enderecos);
    }

    @Test
    void listarEnderecosPorDoador_DeveRetornarListaVazia_QuandoDoadorNaoTemEnderecos() {
        when(doadorRepository.existsById(1L)).thenReturn(true);
        when(enderecoRepository.findByDoadorId(1L)).thenReturn(Arrays.asList());
        when(enderecoMapper.toDTOList(Arrays.asList())).thenReturn(Arrays.asList());

        List<EnderecoDoadorDTO> resultado = enderecoService.listarEnderecosPorDoador(1L);

        assertThat(resultado).isEmpty();

        verify(doadorRepository, times(1)).existsById(1L);
        verify(enderecoRepository, times(1)).findByDoadorId(1L);
        verify(enderecoMapper, times(1)).toDTOList(Arrays.asList());
    }

    @Test
    void listarEnderecosPorDoador_DeveLancarBusinessException_QuandoDoadorNaoExiste() {
        when(doadorRepository.existsById(999L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enderecoService.listarEnderecosPorDoador(999L));

        assertThat(exception.getMessage()).isEqualTo("Doador não encontrado com ID: 999");

        verify(doadorRepository, times(1)).existsById(999L);
        verify(enderecoRepository, never()).findByDoadorId(anyLong());
        verify(enderecoMapper, never()).toDTOList(any());
    }
}