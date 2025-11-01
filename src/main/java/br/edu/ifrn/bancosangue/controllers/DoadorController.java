package br.edu.ifrn.bancosangue.controllers;

import br.edu.ifrn.bancosangue.domain.entities.Doador;
import br.edu.ifrn.bancosangue.services.DoadorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doadores")
public class DoadorController {

    private final DoadorService doadorService;

    public DoadorController(DoadorService doadorService) {
        this.doadorService = doadorService;
    }

    @PostMapping
    public Doador createDoador(@Valid @RequestBody Doador doador) {
        return doadorService.criarDoador(doador);
    }

    @GetMapping
    public List<Doador> getAllDoadores() {
        return doadorService.listaDoadores();
    }

    @PutMapping("/{id}")
    public Doador updateDoador(@PathVariable Long id, @RequestBody Doador dadosAtualizados) {
        return doadorService.atualizarDoador(id, dadosAtualizados);
    }

    @DeleteMapping("/{id}")
    public void deleteDoador(@PathVariable Long id) {
        doadorService.deletarDoador(id);
    }


}
