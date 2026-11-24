package tn.esprit.autoloc.autolocapi.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDto;
import tn.esprit.autoloc.autolocapi.web.mapper.VehiculeMapper;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@Tag(name = "Véhicules")
public class VehiculeController {
    private final IVehiculeService service;
    private final VehiculeMapper mapper;
    private final IAgenceRepository agenceRepository;

    public VehiculeController(IVehiculeService service, VehiculeMapper mapper, IAgenceRepository agenceRepository) {
        this.service = service;
        this.mapper = mapper;
        this.agenceRepository = agenceRepository;
    }

    @GetMapping
    public List<VehiculeDto> findAll() { return mapper.toDtos(service.findAll()); }

    @GetMapping("/{id}")
    public VehiculeDto findById(@PathVariable Long id) { return mapper.toDto(service.findById(id)); }

    @PostMapping
    public ResponseEntity<VehiculeDto> create(@Valid @RequestBody VehiculeDto dto) {
        Vehicule vehicule = toEntity(dto);
        if (!service.check(vehicule, true)) throw new BadRequestException("Données invalides");
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(vehicule)));
    }

    @PutMapping("/{id}")
    public VehiculeDto update(@PathVariable Long id, @Valid @RequestBody VehiculeDto dto) {
        Vehicule vehicule = toEntity(dto);
        vehicule.setIdVehicule(id);
        if (!service.check(vehicule, false)) throw new BadRequestException("Données invalides");
        return mapper.toDto(service.update(id, vehicule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Vehicule toEntity(VehiculeDto dto) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.immatriculation());
        vehicule.setMarque(dto.marque());
        vehicule.setModele(dto.modele());
        vehicule.setCategorie(dto.categorie());
        vehicule.setTarifJournalier(dto.tarifJournalier());
        vehicule.setStatut(dto.statut());
        if (dto.agenceId() != null) {
            Agence agence = agenceRepository.findById(dto.agenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", String.valueOf(dto.agenceId())));
            vehicule.setAgence(agence);
        }
        return vehicule;
    }

    @PutMapping("/{id}/desaffecter")
    @Operation(summary = "Désaffecter l'agence d'un véhicule")
    public Vehicule desaffecter(@PathVariable Long id) { return service.desaffecterAgence(id); }

    @PutMapping("/{idVehicule}/affecter-agence/{idAgence}")
    @Operation(summary = "Affecter une agence à un véhicule")
    public Vehicule affecterAgence(@PathVariable Long idVehicule, @PathVariable Long idAgence) {
        return service.affecterAgence(idVehicule, idAgence);
    }

    @PutMapping("/{idVehicule}/affecter-equipement/{idEquipement}")
    @Operation(summary = "Affecter un équipement à un véhicule")
    public Vehicule affecterEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return service.affecterEquipement(idVehicule, idEquipement);
    }
}
