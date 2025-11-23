package br.edu.ifrn.bancosangue.controllers;

import br.edu.ifrn.bancosangue.controllers.docs.DoadorApiDoc;
import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.domain.entities.EnderecoDoador;
import br.edu.ifrn.bancosangue.dtos.EnderecoDoadorDTO;
import br.edu.ifrn.bancosangue.services.DoadorService;
import br.edu.ifrn.bancosangue.services.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doadores")
public class DoadorController implements DoadorApiDoc {

    private final DoadorService doadorService;
    private final EnderecoService enderecoService;


    public DoadorController(DoadorService doadorService, EnderecoService enderecoService) {
        this.doadorService = doadorService;
        this.enderecoService = enderecoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ATENDENTE')")
    public Doador createDoador(@Valid @RequestBody Doador doador) {
        return doadorService.criarDoador(doador);
    }

    @GetMapping
    public List<Doador> getAllDoadores() {
        return doadorService.listaDoadores();
    }

    @Override
    @GetMapping("/{id}")
    public Doador getDoadorById(Long id) {
        return doadorService.obterDoadorPorId(id);
    }

    @PutMapping("/{id}")
    public Doador updateDoador(@PathVariable Long id, @Valid @RequestBody Doador dadosAtualizados) {
        return doadorService.atualizarDoador(id, dadosAtualizados);
    }

    @DeleteMapping("/{id}")
    public void deleteDoador(@PathVariable Long id) {
        doadorService.deletarDoador(id);
    }


    // Endereço endpoints /api/doadores/{doadorId}/enderecos
    @PostMapping("/{doadorId}/enderecos")
    public EnderecoDoadorDTO adicionarEndereco(@PathVariable Long doadorId,
                                            @Valid @RequestBody EnderecoDoadorDTO enderecoDTO) {
        return enderecoService.criarEndereco(doadorId, enderecoDTO);
    }

}
