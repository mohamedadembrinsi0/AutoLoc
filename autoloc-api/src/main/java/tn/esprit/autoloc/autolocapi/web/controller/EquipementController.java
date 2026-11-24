package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.service.IEquipementService;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import tn.esprit.autoloc.autolocapi.web.CrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/equipements")
@Tag(name = "Équipements")
public class EquipementController extends CrudController<Equipement> {
    private final IEquipementService equipementService;
    private final IVehiculeService vehiculeService;
    public EquipementController(IEquipementService service, IVehiculeService vehiculeService) {
        super(service); this.equipementService = service; this.vehiculeService = vehiculeService;
    }
    @PostMapping("/{equipementId}/vehicules/{vehiculeId}")
    public Vehicule assign(@PathVariable Long equipementId, @PathVariable Long vehiculeId) {
        Equipement equipement = equipementService.findById(equipementId);
        Vehicule vehicule = vehiculeService.findById(vehiculeId);
        vehicule.addEquipement(equipement);
        return vehiculeService.update(vehiculeId, vehicule);
    }
    @DeleteMapping("/{equipementId}/vehicules/{vehiculeId}")
    public Vehicule unassign(@PathVariable Long equipementId, @PathVariable Long vehiculeId) {
        Equipement equipement = equipementService.findById(equipementId);
        Vehicule vehicule = vehiculeService.findById(vehiculeId);
        if (!vehicule.getEquipements().contains(equipement)) throw new BadRequestException("Équipement non affecté à ce véhicule");
        vehicule.removeEquipement(equipement);
        return vehiculeService.update(vehiculeId, vehicule);
    }
}
