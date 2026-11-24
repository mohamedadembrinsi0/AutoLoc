package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.service.IAgenceService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceDto;
import tn.esprit.autoloc.autolocapi.web.mapper.AgenceMapper;

import java.util.List;

@RestController
@RequestMapping("/agences")
@Tag(name = "Agences", description = "Gestion des agences")
public class AgenceController {
    private final IAgenceService service;
    private final AgenceMapper mapper;
    public AgenceController(IAgenceService service, AgenceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AgenceDto> findAll() { return mapper.toDtos(service.findAll()); }

    @GetMapping("/{id}")
    public AgenceDto findById(@PathVariable Long id) { return mapper.toDto(service.findById(id)); }

    @PostMapping
    public org.springframework.http.ResponseEntity<AgenceDto> create(@RequestBody AgenceDto dto) {
        Agence agence = mapper.toEntity(dto);
        if (!service.check(agence, true)) throw new BadRequestException("Données invalides");
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(mapper.toDto(service.create(agence)));
    }

    @PutMapping("/{id}")
    public AgenceDto update(@PathVariable Long id, @RequestBody AgenceDto dto) {
        Agence agence = service.findById(id);
        mapper.updateEntity(dto, agence);
        if (!service.check(agence, false)) throw new BadRequestException("Données invalides");
        return mapper.toDto(service.update(id, agence));
    }

    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/has-vehicules")
    @Operation(summary = "Vérifier si une agence possède des véhicules")
    public boolean hasVehicules(@PathVariable Long id) { return service.hasVehicules(id); }

    @GetMapping(value = "/{id}/simulation", produces = "text/plain")
    @Operation(summary = "Simuler le tarif des véhicules d'une agence")
    public String simuler(@PathVariable Long id, @RequestParam int nombreJours, @RequestParam VehiculeOffre offre) {
        return service.simuler(id, nombreJours, offre);
    }

    @PostMapping("/{id}/employes")
    @Operation(summary = "Ajouter plusieurs employés à une agence dans une transaction")
    public Agence addEmployes(@PathVariable Long id, @RequestBody EmployesRequest request) {
        if (request == null || request.agence() == null || request.employes() == null)
            throw new BadRequestException("L'agence et la liste des employés sont obligatoires");
        return service.addEmployes(id, request.agence(), request.employes());
    }

    @PutMapping(value = "/{id}", consumes = "application/vnd.autoloc.employes+json")
    public Agence updateWithEmployes(@PathVariable Long id, @RequestBody EmployesRequest request) {
        if (request == null || request.agence() == null || request.employes() == null)
            throw new BadRequestException("L'agence et la liste des employés sont obligatoires");
        return service.simuler(id, request.agence(), request.employes());
    }

    public record EmployesRequest(Agence agence, List<Employe> employes) { }
}
